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

# Override if the tests should install and run in a different namespace.
# If not specified the 'default' namespace will be used
# The main script will take care of
#
# Parameters
# 1 - the name of the qs directory
function namespace() {
  application="${1}"
  # This matches the namespace used to generate charts/strimzi-on-kubernetes.yaml
  # We use a separate namespace since it installs so much!
  echo "kafka"
}


# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function installPrerequisites()
{
  application="${1}"

  echo "Installing Strimzi operator"
  kubectl apply -f charts/strimzi-on-kubernetes.yaml --wait --timeout=10m0s

  seconds=300
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Looping for 5 minutes until the 'kafka' CRD is available "
  while [ $now -lt $end ]; do
    sleep 5
    echo "Create a Strimzi instance and testing topic"
    kubectl apply -f ./charts/kafka-on-kubernetes.yaml --wait --timeout=10m0s
    break
    if [ "$?" = "0" ]; then
      break
    fi
  done

  seconds=900
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Looping for 15 minutes until the Kafka cluster is ready"
  while [ $now -lt $end ]; do
    sleep 15
    now=$(date +%s)

    echo "Checking if pods are ready"

    # Check the entity operator exists. It will have a name like my-cluster-entity-operator-<pod suffix>
    # We do this check first because it takes a while to appear
    kubectl get pods -l app.kubernetes.io/instance='my-cluster',app.kubernetes.io/name='entity-operator' | grep "my-cluster-entity-operator" || continue

    # Wait 10 seconds for all pods to come up, and renter the loop if not
    kubectl wait pod -l app.kubernetes.io/instance='my-cluster' --for=condition=Ready --timeout=10s || continue

    # If we got here, everything is up, so we can proceed
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
  kubectl delete -f ./charts/kafka-on-kubernetes.yaml --wait --timeout=10m0s
  kubectl delete -f charts/strimzi-on-kubernetes.yaml --wait --timeout=10m0s
}
