function applicationName() {
  #echo "${1}"
  # The fill microprofile-reactive-messaging-kafka name results in names of generated resources which are too long for
  # OpenShift to handle
  echo "ejb-txn-remote-call"
}

function installPrerequisites()
{
  application="${1}"

  CURRENT_FOLDER=$(pwd)

  echo "Installing the PostgreSQL database"

  # Install bitnami and PostgreSQL
  helm repo add bitnami https://charts.bitnami.com/bitnami
  helm install postgresql bitnami/postgresql -f charts/postgresql.yaml --wait --timeout="${helm_install_timeout}"

  echo "Cloning WildFly operator repository in /tmp"

  cd /tmp || return
  git clone https://github.com/wildfly/wildfly-operator.git

  cd wildfly-operator || return

  echo "Installing and deploying the 'WildFly operator' to the cluster"

  # This seems to be an OpenShift 'extra'
  # kubectl adm policy add-cluster-role-to-user cluster-admin developer
  make install || return
  make deploy || return

  seconds=120
  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Waiting for 2 minutes that the 'WildFly operator' pod is in the 'Running' status."
  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(kubectl get pods --field-selector=status.phase==Running -l name=wildfly-operator | awk '{ if ($3 == "Running" && $2 == "1/1") { print } }') ]]; then
      break
    fi

    now=$(date +%s)
  done

  cd $CURRENT_FOLDER || return
}

function customProvisionServer() {
  echo 1
}

function provisionServer() {
  application="${1}"

  echo "Packaging the 'client' provisioned server..."
  cd client || return 1
  mvn -B -fae clean package -Popenshift,provisioned-server wildfly:image -DskipTests -DpostgresqlUsername="test" -DpostgresqlPassword="test" -DpostgresqlHost="postgresql"

  export client_image_name="${application}-client"
  export client_image_location="localhost:5000/${client_image_name}"
  export image="${client_image_location}:latest"

  echo "Building and tagging the 'client' image..."
  docker build --load -t ${image} ./target
  echo "...and pushing to registry."
  docker push ${image}

  cd ../server || return 1
  echo "Packaging the 'server' provisioned server..."
  mvn -B -fae clean package -Popenshift,provisioned-server wildfly:image -DskipTests -DpostgresqlUsername="test" -DpostgresqlPassword="test" -DpostgresqlHost="postgresql"

  export server_image_name="${application}-server"
  export server_image_location="localhost:5000/${server_image_name}"
  export image="${server_image_location}:latest"

  echo "Building and tagging 'server' image..."
  docker build --load -t ${image} ./target
  echo "...and pushing to registry."
  docker push ${image}

  # Let's go back to the root of the quickstart
  cd ..
}

function customDeploy() {
  echo 1
}

function deploy() {
  seconds=60

  kubectl create -f client/client-cr.yaml

  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Waiting for 1 minutes that the 'ejb-txn-remote-call-client' pod is in 'Running' status."

  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(kubectl get pods --field-selector=status.phase==Running -l app.kubernetes.io/name=ejb-txn-remote-call-client | awk '{ if ($3 == "Running" && $2 == "1/1") { print } }') ]]; then
      break
    fi

    now=$(date +%s)
  done

  kubectl create -f server/server-cr.yaml

  now=$(date +%s)
  end=$(($seconds + $now))

  echo "Waiting for 1 minutes that the 'ejb-txn-remote-call-server' pods are in 'Running' status."

  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(kubectl get pods --field-selector=status.phase==Running -l app.kubernetes.io/name=ejb-txn-remote-call-server | awk '{ if ($3 == "Running" && $2 == "1/1") { print } }') ]]; then
      break
    fi

    now=$(date +%s)
  done
}

function helmInstall() {
  echo "Nothing to do in helmInstall()..."
}

function customPortForward() {
  echo 1
}

function portForward() {

  seconds=60

  # Start the first port-forward process for the client
  nohup kubectl port-forward service/${application}-client-headless 8080:8080 > /dev/null 2>&1 &
  kubectl_fwd_pid_client=$!

  now=$(date +%s)
  end=$(($seconds + $now))
  localhost="localhost"
  port="8080"
  endpoint="/client/remote-outbound-stateless"

  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(curl --silent --write-out "%{http_code}" --output /dev/null "http://${localhost}:${port}${endpoint}") -eq 200 ]]; then
      break
    fi

    now=$(date +%s)
  done

  # Start the second port-forward process for the server
  nohup kubectl port-forward service/${application}-server-headless 8180:8080 > /dev/null 2>&1 &
  kubectl_fwd_pid_server=$!

  now=$(date +%s)
  end=$(($seconds + $now))
  localhost="localhost"
  port="8180"
  endpoint="/server/commits"

  while [ $now -lt $end ]; do
    sleep 5

    if [[ $(curl --silent --write-out "%{http_code}" --output /dev/null "http://${localhost}:${port}${endpoint}") -eq 200 ]]; then
      break
    fi

    now=$(date +%s)
  done

  echo "${kubectl_fwd_pid_client} ${kubectl_fwd_pid_server}"
}

function customRunningTests() {
  echo 1
}

function runningTests() {
  application="${1}"
  server_protocol="${2}"
  extraMvnVerifyArguments="${3}"

  cd client || return 1
  route="localhost:8080"

  mvnVerifyArguments="-Dserver.host=${server_protocol}://${route} "
  if [ -n "${extraMvnVerifyArguments}" ]; then
    mvnVerifyArguments="${mvnVerifyArguments} ${extraMvnVerifyArguments}"
  fi
  if [ "${QS_DEBUG_TESTS}" = "1" ]; then
    mvnVerifyArguments="${mvnVerifyArguments} -Dmaven.failsafe.debug=true"
  fi

  echo "Verify Arguments: ${mvnVerifyArguments}"

  mvn -B verify -Pintegration-testing ${mvnVerifyArguments}

  client_test_status="$?"

  if [ "$?" != "0" ]; then
    client_test_status=1
    echo "Tests failed!"
    echo "Dumping the application pod"
    kubectl logs deployment/"${application}-client-0"
    testsFailed
  fi

  cd ../server || return 1
  route="localhost:8180"

  mvnVerifyArguments="-Dserver.host=${server_protocol}://${route} "
  if [ -n "${extraMvnVerifyArguments}" ]; then
    mvnVerifyArguments="${mvnVerifyArguments} ${extraMvnVerifyArguments}"
  fi
  if [ "${QS_DEBUG_TESTS}" = "1" ]; then
    mvnVerifyArguments="${mvnVerifyArguments} -Dmaven.failsafe.debug=true"
  fi

  echo "Verify Arguments: ${mvnVerifyArguments}"

  mvn -B verify -Pintegration-testing ${mvnVerifyArguments}

  server_test_status="$?"

  if [ "$?" != "0" ]; then
    server_test_status=1
    echo "Tests failed!"
    echo "Dumping the application pods"
    kubectl logs deployment/"${application}-server-0"
    kubectl logs deployment/"${application}-server-1"
    testsFailed
  fi

  if (( client_test_status == 1 || server_test_status == 1 )); then
    result=1
  else
    result=0
  fi

  return "${result}"
}

function customHelmUninstall() {
  return 0
}

function helmUninstall() {
  echo "helmUninstall() nothing to do"
}

# Cleans any prerequisites after doing the Helm uninstall.
# The current directory is the quickstart directory
#
# Parameters
# 1 - application name
function cleanPrerequisites()
{
  cd /tmp/wildfly-operator || return

  make undeploy
  
  cd ..
  rm -rf wildfly-operator

  # Uninstall PostgreSQL and remove bitnami
  helm uninstall postgresql
  helm repo remove bitnami
}
