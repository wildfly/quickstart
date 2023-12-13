# Use insecure route for this QS
function getExtraHelmSetArguments() {
  echo "--set deploy.route.tls.enabled=false"
}
