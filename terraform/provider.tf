provider "kubernetes" {
  # Usa el mismo kubeconfig que usa kubectl (~/.kube/config), y el contexto
  # del cluster local de k3d. Asi Terraform administra el mismo cluster
  # que ya usas a mano con kubectl.
  config_path    = var.kubeconfig_path
  config_context = var.kube_context
}
