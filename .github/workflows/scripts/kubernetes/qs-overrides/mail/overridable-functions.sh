# These functions are 'overridable in the individual quickstarts.
# To do so create a ./qs-overrides/${qs_dir}/overridable-functions.sh and override the
# functions you need to. ${qs_dir} in this case is the same as the name of the quickstart directory
# that you want to tweak

# Installs any prerequisites before doing the Helm install.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function installPrerequisites()
{
  application="${1}"

  echo "Installing greenmail mail server"
  kubectl apply -f charts/greenmail-kubernetes.yaml --wait --timeout=10m0s
}

# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function cleanPrerequisites()
{
  application="${1}"

  echo "Uninstalling greenmail mail server"
  kubectl delete -f charts/greenmail-kubernetes.yaml --wait --timeout=10m0s
}