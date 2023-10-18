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
  application="${1}"
  echo "Creating amq-streams-operator-group"

  oc apply -f - <<EOF
  apiVersion: operators.coreos.com/v1
  kind: OperatorGroup
  metadata:
    name: amq-streams-operator-group
    namespace: default
  spec: {}
EOF

  echo "Creating amq-streams-subscription"
  oc apply -f - <<EOF
  apiVersion: operators.coreos.com/v1alpha1
  kind: Subscription
  metadata:
    name: amq-streams-subscription
    namespace: default
  spec:
    channel: stable
    installPlanApproval: Automatic
    name: amq-streams
    source: redhat-operators
    sourceNamespace: openshift-marketplace
    startingCSV: amqstreams.v2.5.0-0
EOF


  seconds=120
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Looping for 2 minutes until the 'kafka' CRD is available "
  while [ $now -lt $end ]; do
    # It takes a while for the kafka CRD to be ready
    sleep 5
    echo "Trying to create my-cluster"
    oc apply -f - <<EOF
    apiVersion: kafka.strimzi.io/v1beta2
    kind: Kafka
    metadata:
      name: my-cluster
    spec:
      kafka:
        replicas: 3
        listeners:
          - name: plain
            port: 9092
            type: internal
            tls: false
        storage:
          type: ephemeral
      zookeeper:
        replicas: 3
        storage:
          type: ephemeral
      entityOperator:
        topicOperator: {}
EOF
    if [ "$?" = "0" ]; then
      break
    fi
    now=$(date +%s)
  done

  echo "Creating testing topic"
  oc apply -f - <<EOF
  apiVersion: kafka.strimzi.io/v1beta2
  kind: KafkaTopic
  metadata:
    name: testing
  labels:
    strimzi.io/cluster: my-cluster
  spec:
    partitions: 3
    replicas: 3
EOF

  # Wait for the pods to come up
  seconds=900
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Looping for 15 minutes until the Kafka cluster is ready"
  while [ $now -lt $end ]; do
    sleep 15
    echo "Checking if pods are ready"

    # Check the entity operator exists. It will have a name like my-cluster-entity-operator-<pod suffix>
    # We do this check first because it takes a while to appear
    oc get pods -l app.kubernetes.io/instance='my-cluster',app.kubernetes.io/name='entity-operator' | grep "my-cluster-entity-operator" || continue

    # Wait 10 seconds for all pods to come up, and renter the loop if not
    oc wait pod -l app.kubernetes.io/instance='my-cluster' --for=condition=Ready --timeout=10s || continue

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
  # TODO There are a few topics created that need cleaning up

  oc delete kafka my-cluster
  oc delete subscription amq-streams-subscription
  oc delete operatorgroup amq-streams-operator-group
  oc delete deployment amq-streams-cluster-operator-v2.5.0-1
}
