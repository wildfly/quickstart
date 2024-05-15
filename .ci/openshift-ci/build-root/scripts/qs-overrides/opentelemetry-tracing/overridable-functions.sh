function runPostHelmInstallCommands() {
  echo "Applying all OpenTelemetry Collector resources"
  oc apply -f opentelemetry-collector-openshift.yaml
}

function cleanPrerequisites() {
  echo "Deleting all OpenTelemetry Collector resources"
  oc delete -f opentelemetry-collector-openshift.yaml
}