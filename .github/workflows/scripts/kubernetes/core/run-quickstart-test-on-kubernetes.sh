#!/bin/bash

# This assumes the user has
# * Added the WildFly Helm repository
#
# Understood environment variables:
# * QS_HELM_INSTALL_TIMEOUT - Default is 10m0s
# * QS_HELM_UNINSTALL_TIMEOUT - Default is 10m0s
# * QS_DEBUG_TESTS - Runs the tests with '-Dmaven.failsafe.debug=true'
################################################################################################
# Go into the quickstart directory

test_status=0
script_directory="${0%/*}"
script_directory=$(realpath "${script_directory}")
cd "${script_directory}"
qs_dir="${1}"
if [ -z "${1}" ]; then
  echo "No quickstart directory set"
  exit 1
fi

if [ ! -d "../../../../../${qs_dir}" ]; then
  echo "$(pwd)/../../../../../${qs_dir} does not exist"
  exit 1
fi
cd "../../../../../${qs_dir}"

echo "Running the ${qs_dir} tests on OpenShift"
start=$SECONDS

# Determine timeouts
export helm_install_timeout=10m0s
export helm_uninstall_timeout=10m0s
if [ -n "${QS_HELM_INSTALL_TIMEOUT}" ]; then
  export helm_install_timeout="${QS_HELM_INSTALL_TIMEOUT}"
fi
if [ -n "${QS_HELM_UNINSTALL_TIMEOUT}" ]; then
  export helm_uninstall_timeout="${QS_HELM_UNINSTALL_TIMEOUT}"
fi


################################################################################################
# Load up the helper functions, possibly overridden in the quickstart
if [ ! -f "${script_directory}/overridable-functions.sh" ]; then
  echo "Aborting since no file found at ${script_directory}/overridable-functions.sh"
  exit 1
fi
source "${script_directory}/overridable-functions.sh"

qs_override_file="${script_directory}/../qs-overrides/${qs_dir}/overridable-functions.sh"
echo "Checking for override file in ${qs_override_file}..."
if [ -f "${qs_override_file}" ]; then
  echo "Loading overrides"
  source "${qs_override_file}"
fi

# These functions are from overridable-functions.sh
application=$(applicationName "${qs_dir}")
namespace=$(namespace "${application}")
helm_set_arg_prefix=$(getHelmSetVariablePrefix)
server_protocol="http"
disableTlsRoute=$(disableTlsRoute)
if [ "1" = "${disableTlsRoute}" ]; then
  server_protocol="http"
fi


if [ -n "${namespace}" ]; then
    old_namespace="$(kubectl config view --minify --output 'jsonpath={..namespace}'; echo)"
    echo "Switching to '${namespace}' namespace from '${old_namespace}'"
    kubectl create namespace "${namespace}"
    kubectl config set-context --current --namespace="${namespace}"
fi


################################################################################################
# Install any pre-requisites. Function is from overridable-functions.sh
echo "Checking if we need to install pre-requisites"
installPrerequisites "${application}"

################################################################################################
# Provision server and push imagestream

echo "Building application and provisioning server image..."
mvn -B package -Popenshift wildfly:image -DskipTests

echo "Tagging image and pushing to registry..."
export root_image_name="localhost:5000/${application}"
export image="${root_image_name}:latest"
docker tag ${qs_dir} ${image}
docker push ${image}

echo "Creating docker file locally and pushing to registry at localhost:5000"
docker build -t "${image}" target

################################################################################################
# Helm install, waiting for the pods to come up
helm_set_arguments=" --set ${helm_set_arg_prefix}build.enabled=false --set ${helm_set_arg_prefix}deploy.route.enabled=false --set ${helm_set_arg_prefix}image.name=${root_image_name}"

additional_arguments="No additional arguments"
if [ -n "${helm_set_arguments}" ]; then
  additional_arguments="Additional arguments: ${helm_set_arguments}"
fi

echo "Performing Helm install and waiting for completion.... (${additional_arguments})"
# helmInstall is from overridable-functions.sh
helm_install_ret=$(helmInstall "${application}" "${helm_set_arguments}")

# For some reason the above sometimes becomes a multi-line string. actual The exit code will be
# on the last line
helm_install_ret=$(echo "${helm_install_ret}"| tail -n 1)

echo "ret: ${helm_install_ret}"
if [ "${helm_install_ret}" != "0" ]; then
  echo "Helm install failed!"
  echo "Dumping the application pod(s)"
  kubectl logs deployment/"${application}"
  helmInstallFailed
fi

kubectl port-forward service/${application} 8080:8080 &
kubectl_fwd_pid=$!
echo "Process ID of kubect port-forward: ${kubectl_fwd_pid}"

################################################################################################
# Run any post install
echo "Running post Helm install commands in $(pwd)"
runPostHelmInstallCommands

################################################################################################
# Run tests
echo "running the tests"
pwd

route="localhost:8080"

mvnVerifyArguments="-Dserver.host=${server_protocol}://${route} "
extraMvnVerifyArguments="$(getMvnVerifyExtraArguments)"
if [ -n "${extraMvnVerifyArguments}" ]; then
  mvnVerifyArguments="${mvnVerifyArguments} ${extraMvnVerifyArguments}"
fi
if [ "${QS_DEBUG_TESTS}" = "1" ]; then
  mvnVerifyArguments="${mvnVerifyArguments} -Dmaven.failsafe.debug=true"
fi

echo "Verify Arguments: ${mvnVerifyArguments}"

mvn -B verify -Pintegration-testing ${mvnVerifyArguments}

if [ "$?" != "0" ]; then
  test_status=1
  echo "Tests failed!"
  echo "Dumping the application pod(s)"
  kubectl logs deployment/"${application}"
  testsFailed
fi

kill -9 ${kubectl_fwd_pid}

################################################################################################
# Helm uninstall
echo "Running Helm uninstall"
helm uninstall "${application}" --wait --timeout=10m0s

################################################################################################
# Clean pre-requisites (cleanPrerequisites is fromm overridable-functions.sh)
echo "Checking if we need to clean pre-requisites"
cleanPrerequisites "${application}"

#
if [ -n "${namespace}" ]; then
    echo "Switching to '${old_namespace}' namespace from '${namespace}'"
    kubectl config set-context --current --namespace="${old_namespace}"
    echo "Deleting namespace '${namespace}'"
fi



################################################################################################
# Delete target directory to conserve disk space when running all the tests
if [ "${SKIP_CLEANUP}" = "1" ]; then
  echo "Skipping cleanup..."
else
  echo "Deleting target directory..."
  rm -rf target
fi

end=$SECONDS
duration=$((end - start))
echo "${application} tests run in $(($duration / 60))m$(($duration % 60))s."

exit ${test_status}