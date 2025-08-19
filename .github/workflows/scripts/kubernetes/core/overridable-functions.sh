# These functions are 'overridable in the individual quickstarts.
# To do so create a ./qs-overrides/${qs_dir}/overridable-functions.sh and override the
# functions you need to. ${qs_dir} in this case is the same as the name of the quickstart directory
# that you want to tweak

# The default is to use the quickstart directory as the name, but in some cases
# a quickstart may need to shorten the name of the application in order to control
# the length of the resources created by OpenShift
#
# Parameters
# 1 - the name of the qs directory (not the full path)
#
function applicationName() {
  echo "${1}"
}


# Override if the tests should install and run in a different namespace.
# If not specified the 'default' namespace will be used
# The main script will take care of
#
# Parameters
# 1 - the name of the qs directory
#
function namespace() {
  application="${1}"
  # Uncomment to make the tests run in the 'testing' namespace
  #echo "testing"
}


# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
#
function installPrerequisites()
{
  application="${1}"
  echo "No prerequisites required for ${application}"
}

# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
#
function cleanPrerequisites()
{
  application="${1}"
  echo "No prerequisites to clean for ${application}"
}

# Trigger the custom behaviour when it comes to
# provision the server and push the imagestream.
# Returns
# 0 - false
# 1 - true
#
function customProvisionServer() {
  echo 0
}

# Provision server and push imagestream
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
# 2 - quickstart dir
#
function provisionServer()
{
  echo "Nothing to do in provisionServer()..."
}

# Trigger a custom behaviour when it comes to
# setting up the environment
# Returns
# 0 - false
# 1 - true
#
function customDeploy() {
  echo 0
}

# Set up the environment before testing
# Parameters
# 1 - application name
#
function deploy() {
  echo "Nothing to do in deploy()..."
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

    # '--wait' waits until the pods are ready
    # `--timeout` sets the timeout for the wait.
    # https://helm.sh/docs/helm/helm_install/ has more details
    # Don't quote ${helm_set_arguments} since then it fails when there are none
    helm install "${application}" wildfly/wildfly -f charts/helm.yaml  --wait --timeout=${helm_install_timeout} ${helm_set_arguments}
    echo "$?"
}

# Commands to run once the Helm install has completed
function runPostHelmInstallCommands() {
    echo "No post helm install commands"
}


# Whether to disable TLS route.
# Default is to not do anything.
# To disable TLS routes, override this method change the body to 'echo "1"'
function disableTlsRoute() {
    echo ""
}

# If the Helm variables set by the parent script (e.g. 'build.enabled') need a prefix, return
# that here. If e.g "wildfly." is returned, the resulting 'build.enabled' becomes 'wildfly.build.enabled'
function getHelmSetVariablePrefix() {
    echo ""
}

# If we need to specify any extra arguments (such as system properties) to the
# 'mvn verify -Pintegration-testing command, specify those here'
function getMvnVerifyExtraArguments() {
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

# Trigger a custom behaviour when it comes to
# forward ports
# Returns
# 0 - false
# 1 - true
#
function customPortForward() {
  echo 0
}

# Port forward to test the quickstart
# Parameters
# 1 - application name
#
function portForward() {
  echo "Nothing to do in portForward()..."
}

# Trigger a custom behaviour when it comes to
# running tests
# Returns
# 0 - false
# 1 - true
#
function customRunningTests() {
  echo 0
}

# Running tests of the quickstart
# Parameters
# 1 - application name
# 2 - server protocol
# 3 - extra maven argument for the verify target
#
function runningTests() {
  echo "Nothing to do in portForward()..."
}

# Trigger a custom behaviour when it comes to
# running tests
# Returns
# 0 - false
# 1 - true
#
function customHelmUninstall() {
  echo 0
}

# Performs the 'helm uninstall' command.
# Parameters
# 1 - application name
#
function helmUninstall() {
  application="${1}"

  helm uninstall "${application}" --wait --timeout=10m0s
}

# More output when the tests have failed
# Parameters
# 1 - application name
#
function testsFailed() {
    echo ""
}
