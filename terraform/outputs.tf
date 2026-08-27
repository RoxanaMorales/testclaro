output "namespace" {
  description = "Namespace creado"
  value       = kubernetes_namespace.testclaro.metadata[0].name
}

output "service_name" {
  description = "Nombre del Service dentro del cluster"
  value       = kubernetes_service.testclaro_service.metadata[0].name
}

output "deployment_name" {
  description = "Nombre del Deployment"
  value       = kubernetes_deployment.testclaro_app.metadata[0].name
}
