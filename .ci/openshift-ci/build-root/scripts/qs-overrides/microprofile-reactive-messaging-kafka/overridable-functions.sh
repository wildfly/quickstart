# These functions are 'overridable in the individual quickstarts.
# To do so create a ./qs-overrides/${qs_dir}/overridable-functions.sh and override the
# functions you need to. ${qs_dir} in this case is the same as the name of the quickstart directory
# that you want to tweak

# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory.
# The default is to use the quickstart directory as the name, but in some cases
# a quickstart may need to shorten the name of the application in order to control
# the length of the resources created by OpenShift
#
# Parameters
# 1 - the name of the qs directory (not the full path)
function applicationName() {
  #echo "${1}"
  # The fill microprofile-reactive-messaging-kafka name results in names of generated resources which are too long for
  # OpenShift to handle
  echo "mp-rm-qs"
}


# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function installPrerequisites()
{
  maxWaitingTimeInSeconds=900 # 15 minutes

  echo "Subscribe the AMQ stream operator"
  oc apply -f ./charts/amq-operator-on-openshift.yaml --wait --timeout=10m0s

  echo "Looping for 15 minutes until the AMQ stream Operator is ready to use"
  now=$(date +%s)
  end=$((maxWaitingTimeInSeconds + $now))
  while [ $now -lt $end ]; do
    sleep 5
    echo "Checking if The AMQ operator is ready"
    # Check the entity operator exists. If Phase is "Succeeded" it is installed and we can continue
    oc get ClusterServiceVersion | grep "Streams for Apache Kafka" | grep "Succeeded" || continue
    echo "The AMQ operator is ready!"
    break
  done


  echo "Create a AMQ streams instance and testing topic"
  oc apply -f ./charts/kafka-on-openshift.yaml --wait --timeout=10m0s

  # Wait for the pods to come up
  now=$(date +%s)
  end=$((maxWaitingTimeInSeconds + $now))
  while [ $now -lt $end ]; do
    sleep 5
    echo "Checking if \"my-cluster-entity-operator\" pod is ready"
    # Check the entity operator exists. And 1/1 instance is ready to use
    oc get pod | grep "my-cluster-entity-operator" | grep "1/1" || continue
    echo "The AMQ stream instance ready!"
    break
  done
}


# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function cleanPrerequisites()
{
    echo "Deleting all AMQ streams resources"
    oc delete -f ./charts/amq-operator-on-openshift.yaml --wait --timeout=10m0s
    oc delete -f ./charts/kafka-on-openshift.yaml --wait --timeout=10m0s
}
