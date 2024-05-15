function runPostHelmInstallCommands() {
  echo "Applying all OpenTelemetry Collector resources"
  kubectl apply -f charts/opentelemetry-collector-kubernetes.yaml
}

function cleanPrerequisites() {
  echo "Deleting all OpenTelemetry Collector resources"
  kubectl delete -f charts/opentelemetry-collector-kubernetes.yaml
}