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

  echo "Creating my-cluster"
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

# TODO some kind of wait on the

}

# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function cleanPrerequisites()
{
  # There are a few topics created that need cleaning up

  oc delete kafka my-cluster
  oc delete subscription amq-streams-subscription
  oc delete operatorgroup amq-streams-operator-group
}

# Performs the 'helm install' command.
# The current directory is the quickstart directory
# Parameters
# 1 - application name
# 2 - set arguments
#
# Returns the exit status of the helm install
#
# Additionally the following env vars may be used:
# * helm_install_timeout - the adjusted timeout for the helm install
#
function helmInstall() {
    application="${1}"
    helm_set_arguments="$2"

    # '--atomic' waits until the pods are ready, and removes everything if something went wrong
    # `--timeout` sets the timeout for the wait.
    # https://helm.sh/docs/helm/helm_install/ has more details
    # Don't quote ${helm_set_arguments} since then it fails when there are none
    helm install "${application}" wildfly/wildfly -f charts/helm.yaml  --wait --timeout=${helm_install_timeout} ${helm_set_arguments}
    echo "$?"
}

# Checks whether optimized mode should be disabled
# To disable optimized mode for a quickstart, add this method to its
# overridable-functions.sh and change the body to 'echo "1"'
function isOptimizedModeDisabled() {
  echo "0"
}

# If the Helm variables set by the parent script (e.g. 'build.enabled') need a prefix, return
# that here. If e.g "wildfly." is returned, the resulting 'build.enabled' becomes 'wildfly.build.enabled'
function getHelmSetVariablePrefix() {
    echo ""
}

# More output when the helm install has gone wrong
# Parameters
# 1 - application name
#
function helmInstallFailed() {
    # Noop - the main work is done elsewhere
    echo ""
}