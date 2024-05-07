function installPrerequisites()
{
  echo "Installing Artemis"
  kubectl run artemis --env AMQ_USER=admin --env AMQ_PASSWORD=admin --image=quay.io/artemiscloud/activemq-artemis-broker-kubernetes  --port=61616 --expose=true
  kubectl wait --for=condition=Ready pod/artemis --timeout=180s
  # Ideally we would wait for the pod to come up here. But as the Helm build of the main application takes
  # so long, we should have plenty of time for the above command to complete. At least for now.
}


function cleanPrerequisites()
{
  echo "Removing Artemis"
  kubectl delete service artemis
  kubectl delete pod artemis
}
