variable "kubeconfig_path" {
  description = "Ruta al kubeconfig local (el mismo que usa kubectl)"
  type        = string
  default     = "~/.kube/config"
}

variable "kube_context" {
  description = "Contexto de kubectl a usar (el cluster de k3d, ej: k3d-testclaro)"
  type        = string
  default     = "k3d-testclaro"
}

variable "namespace" {
  description = "Namespace de Kubernetes donde se despliega la app"
  type        = string
  default     = "testclaro"
}

variable "image" {
  description = "Imagen de la app publicada en GHCR"
  type        = string
  default     = "ghcr.io/roxanamorales/testclaro:latest"
}

variable "replicas" {
  description = "Cantidad de replicas del deployment"
  type        = number
  default     = 1
}

variable "oracle_datasource_url" {
  description = "URL JDBC de Oracle vista desde dentro del cluster (host.k3d.internal apunta a tu PC)"
  type        = string
  default     = "jdbc:oracle:thin:@host.k3d.internal:1521/FREEPDB1"
}

# --- Secretos ---
# Igual que con .env/.env.example en docker-compose: estos valores NUNCA se
# comitean. Se definen en terraform.tfvars (gitignorado) a partir de
# terraform.tfvars.example.

variable "db_username" {
  description = "Usuario de la base de datos Oracle (APP_USER)"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Password de la base de datos Oracle (APP_USER_PASSWORD)"
  type        = string
  sensitive   = true
}

variable "ghcr_username" {
  description = "Usuario de GitHub para autenticarse en ghcr.io (imagePullSecret)"
  type        = string
  sensitive   = true
}

variable "ghcr_token" {
  description = "Personal Access Token de GitHub con permiso read:packages"
  type        = string
  sensitive   = true
}
