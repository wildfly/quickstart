function runPostHelmInstallCommands() {
    oc apply -f charts/opentelemetry-collector.yaml
}

function cleanPrerequisites()
{
  echo "Removing all micrometer resources"
  oc delete route otelcol-otlp-grpc
  oc delete route otelcol-otlp-http
  oc delete route otelcol-prometheus
  oc delete service opentelemetrycollector
  oc delete deployment opentelemetrycollector
  oc delete configmap collector-config
}
