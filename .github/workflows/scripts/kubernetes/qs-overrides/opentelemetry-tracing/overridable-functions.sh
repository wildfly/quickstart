function runPostHelmInstallCommands() {
  echo "Applying all OpenTelemetry Collector resources"
  oc apply -f charts/opentelemetry-collector-kubernetes.yaml
}

function cleanPrerequisites() {
  echo "Deleting all OpenTelemetry Collector resources"
  oc delete -f charts/opentelemetry-collector-kubernetes.yaml
}