# Este archivo describe, de forma declarativa, los mismos recursos que
# tenemos a mano en k8s/*.yaml. La idea de IaC es esta: en vez de correr
# "kubectl apply" manifest por manifest, Terraform calcula el estado actual
# del cluster contra este archivo y aplica solo la diferencia.

resource "kubernetes_namespace" "testclaro" {
  metadata {
    name = var.namespace
  }
}

resource "kubernetes_config_map" "testclaro_config" {
  metadata {
    name      = "testclaro-config"
    namespace = kubernetes_namespace.testclaro.metadata[0].name
  }

  data = {
    SPRING_PROFILES_ACTIVE = "docker"
    SPRING_DATASOURCE_URL  = var.oracle_datasource_url
  }
}

resource "kubernetes_secret" "oracle_credentials" {
  metadata {
    name      = "oracle-credentials"
    namespace = kubernetes_namespace.testclaro.metadata[0].name
  }

  data = {
    username = var.db_username
    password = var.db_password
  }

  type = "Opaque"
}

# Secret tipo docker-registry para poder pullear la imagen privada de ghcr.io
resource "kubernetes_secret" "ghcr_credentials" {
  metadata {
    name      = "ghcr-credentials"
    namespace = kubernetes_namespace.testclaro.metadata[0].name
  }

  type = "kubernetes.io/dockerconfigjson"

  data = {
    ".dockerconfigjson" = jsonencode({
      auths = {
        "ghcr.io" = {
          username = var.ghcr_username
          password = var.ghcr_token
          auth     = base64encode("${var.ghcr_username}:${var.ghcr_token}")
        }
      }
    })
  }
}

resource "kubernetes_deployment" "testclaro_app" {
  metadata {
    name      = "testclaro-app"
    namespace = kubernetes_namespace.testclaro.metadata[0].name
    labels = {
      app = "testclaro-app"
    }
  }

  spec {
    replicas = var.replicas

    selector {
      match_labels = {
        app = "testclaro-app"
      }
    }

    template {
      metadata {
        labels = {
          app = "testclaro-app"
        }
      }

      spec {
        image_pull_secrets {
          name = kubernetes_secret.ghcr_credentials.metadata[0].name
        }

        container {
          name  = "testclaro-app"
          image = var.image

          port {
            container_port = 8080
          }

          env_from {
            config_map_ref {
              name = kubernetes_config_map.testclaro_config.metadata[0].name
            }
          }

          env {
            name = "SPRING_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.oracle_credentials.metadata[0].name
                key  = "username"
              }
            }
          }

          env {
            name = "SPRING_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = kubernetes_secret.oracle_credentials.metadata[0].name
                key  = "password"
              }
            }
          }

          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 20
            period_seconds        = 10
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 30
            period_seconds        = 20
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "testclaro_service" {
  metadata {
    name      = "testclaro-service"
    namespace = kubernetes_namespace.testclaro.metadata[0].name
  }

  spec {
    selector = {
      app = "testclaro-app"
    }

    port {
      port        = 8080
      target_port = 8080
    }

    type = "ClusterIP"
  }
}
