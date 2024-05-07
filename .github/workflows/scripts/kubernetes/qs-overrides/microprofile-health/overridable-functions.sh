function runPostHelmInstallCommands() {
    kubectl apply -f charts/management-kubernetes.yml
    kubectl port-forward service/microprofile-health-management 9990:9990 &
    export kubectl_fwd_mgmt_pid=$!
}


function getMvnVerifyExtraArguments()
{
  echo "-Dserver.management.host=${server_protocol}://localhost:9990"
}


function cleanPrerequisites()
{
  echo "Removing microprofile-health-management service and port forward"
  kubectl delete service microprofile-health-management
  set -x
  kill -9 "${kubectl_fwd_mgmt_pid}"
  set +x
}
