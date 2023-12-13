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
  echo "${1}"
}


# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
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
function cleanPrerequisites()
{
  application="${1}"
  echo "No prerequisites to clean for ${application}"
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

# Checks whether optimized mode should be disabled
# To disable optimized mode for a quickstart, add this method to its
# overridable-functions.sh and change the body to 'echo "1"'
function isOptimizedModeDisabled() {
  echo "0"
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

# More output when the tests have failed
# Parameters
# 1 - application name
#
function testsFailed() {
    echo ""
}