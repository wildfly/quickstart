#!/bin/sh
# The main script to run the quickstarts
# It iterates over all found quickstart directories, ignoring the ones found in
# excluded-directories.txt, and runs the run-quickstart-test-on-openshift-for each of them
#
# To debug the test script if something goes wrong with a pull request, add a file to the quickstarts root
# called 'enable-wait', which will cause everything to pause for an hour before running any tests.

RED_CONSOLE="\033[0;31m"
GREEN_CONSOLE="\033[0;32m"
NOCOLOUR_CONSOLE="\033[0m"



poll_marker_files() {
  seconds=$1
  now=$(date +%s)
  end=$(($seconds + $now))

  found_file=""
  while [ $now -lt $end ]; do
    sleep 1
    now=$(date +%s)
    if [ -f "continue" ]; then
      found_file="continue"
      break
    elif [ -f "exit" ]; then
      found_file="exit"
      break
    fi
  done
  echo ${found_file}
}

wait_marker_files() {
  echo "Waiting $seconds. Go to this pod's terminal in the console, and either 'touch continue' to stop waiting and proceed with the tests, or 'touch exit' to abort the test run. The latter will result in the test being reported as failed"
  echo "You can log in to the cluster started by the CI to diagnose problems with the following commands"
  echo "oc login $TEST_CLUSTER_URL -u $SYSADMIN_USERNAME -p $SYSADMIN_PASSWORD --insecure-skip-tls-verify"
  #echo "oc get pods"
  #echo "oc rsh pod/<name of pod>"

  found_file=$(poll_marker_files $1)
  if [ -z "${found_file}" ]; then
      echo "Wait timed out - continuing"
  elif [ "${found_file}" = "exit" ]; then
      echo "'exit' marker file found, exiting the test run"
      exit 1
  elif [ "${found_file}" = "continue" ]; then
      echo "'continue' marker file found, continuing the test run"
  fi
}


# Runs a single quickstart
#
# Parameters
# 1 - the directory containing this script
# 2 - the name of the quickstart directory
runQuickstart() {
  script_dir="${1}"
  qs_dir="${2}"

  echo "================================================================="
  echo "Running tests for ${qs_dir} "
  echo "================================================================="


  "${script_dir}"/run-quickstart-test-on-openshift.sh "${qs_dir}"
  if [ "$?" = "0" ]; then
    echo "${GREEN_CONSOLE}Tests for ${qs_dir} PASSED!${NOCOLOUR_CONSOLE}"
  else
    echo "${RED_CONSOLE}Tests for ${qs_dir} FAILED!${NOCOLOUR_CONSOLE}"
    if [ -z "${failed_tests}" ]; then
      failed_tests="${fileName}"
    else
      failed_tests="${failed_tests}, ${fileName}"
    fi
  fi
  echo "---------------------------------------------------------------"
  echo
  echo
}

declare -a test_directories

# Initialises the test_directories variable if this is for a scheduled job
getAllDirectories() {
  for file in ${basedir}/*; do
    fileName=$(basename "${file}")
    if [ ! -d "${file}" ]; then
      # echo "${fileName} is not a directory!"
      continue
    fi
    test_directories+=(${fileName})
  done
}


# Initialises the test_directories variable if this is for a pull request
getPrTouchedDirs() {
  echo "Pull request detected. Determining which directories to run tests for..."
  git remote show upstream &> /dev/null
  if [ "$?" != "0" ]; then
    # On CI this will always happen. This is just here for local testing
    git remote add upstream "${OPENSHIFT_BUILD_SOURCE}"
  fi
  git fetch upstream "${PULL_BASE_REF}" &> /dev/null
  changed_files=$(git diff --name-only  upstream/"${PULL_BASE_REF}"..HEAD)

  declare -a changed_directories

  root_dir_file_changed=0
  for file in ${changed_files}; do
    # DON'T quote the expression, or it doesn't filter!
    if [[ "${file}" == *.adoc ]] || [[ "${file}" == .ci/* ]] || [[ "${file}" == .github/* ]]; then
      continue
    fi

    IFS='/' read -ra parts <<< "${file}"
    if [ "${#parts[@]}" == 1 ]  && [ "${parts[0]}" != 'enable-wait' ] && [ "${parts[0]}" != 'continue' ] && [ "${parts[0]}" != '.gitignore' ] ; then
      echo "Changed detected in ${file} which is in the root directory. All tests will need to be run."
      root_dir_file_changed=1
      break
    else
      changed_directories+=(${parts[0]})
    fi
  done


  if [ "${root_dir_file_changed}" == "1" ]; then
    # Function sets the test_directories variable for us
    getAllDirectories
  else
    # Dedupe the found directories
    output_dirs=$(printf "%s\n" "${changed_directories[@]}" | sort -u)
    declare -a tmp
    for dir in ${output_dirs}; do
      tmp+=(${dir})
    done
    test_directories=(${tmp[@]})
  fi
}

# Repopulates the test_directories variable with valid directories
filterDirectories() {
  declare -a tmp
  for fileName in "${test_directories[@]}"; do
      # Quickstarts that have not been migrated yet
      grep -q "^${fileName}$" excluded-directories.txt
      is_in_excluded="$?"
      if [ "${is_in_excluded}" = "0" ] || [ ! -f "${basedir}/.github/workflows/quickstart_${fileName}_ci.yml" ]; then
        # echo "Skipping ${fileName}!"
        continue
      fi

      if [ ! -d "${basedir}/${fileName}/charts" ]; then
        continue
      fi


      tmp+=(${fileName})
  done
  test_directories=(${tmp[@]})
}

start=$SECONDS
failed_tests=""
script_directory="${0%/*}"
script_directory=$(realpath "${script_directory}")
cd "${script_directory}"
basedir="${script_directory}/../../../.."

if [ -f "${basedir}/enable-wait" ]; then
  # If we find the enable-wait marker file, block for an hour for investigation
  pushd ${basedir}
  wait_marker_files 3600
  popd 
fi

if [ "${JOB_TYPE}" = "presubmit" ]; then
  getPrTouchedDirs

  if [ -z "${PULL_NUMBER}" ]; then
    # Only relevant if running locally for development
    echo "Pull request number not specified in PULL_NUMBER"
    exit 1
  fi
  if [ -z "${JOB_SPEC}" ]; then
    # Again only for local debug
    api_url="https://api.github.com/repos/wildfly/quickstart/pulls/${PULL_NUMBER}"
  else
    # JOB_SPEC will be set on CI
    api_url="https://api.github.com/repos/$(echo $JOB_SPEC | jq -r .refs.org)/$(echo $JOB_SPEC | jq -r .refs.repo)/pulls/${PULL_NUMBER}"
  fi
  # Figure out the original repository and branch of the pull request. OpenShift CI does not give us the information to figure out the
  # name of the repository from which the PR was opened, if it was cloned to e.g. kabir/wildfly-qs rather than simply kabir/quickstart
  echo "Getting PR info from ${api_url}"
  curl -L -H "Accept: application/vnd.github+json" -H "X-GitHub-Api-Version: 2022-11-28" "${api_url}" > .pr_info.json
  export QS_BUILD_URI=$(jq -r .head.repo.clone_url .pr_info.json)
  export QS_BUILD_REF=$(jq -r .head.ref .pr_info.json)
else
# For now just handle everything that is not a pull request to run all jobs
  #elif [ "${JOB_TYPE}" = "periodic" ] || [ "${JOB_TYPE}" = "postsubmit" ]; then
  getAllDirectories
fi

filterDirectories

echo "Parsed test directories, and determined tests should be run for the following directories:"
printf "\t%s\n" "${test_directories[@]}"
echo "Running tests..."

for fileName in "${test_directories[@]}"; do
  if [ "${DRY_RUN}" = "1" ]; then
    echo "${fileName}"
  else
    runQuickstart "${script_directory}" "${fileName}"
    if [ -f "${basedir}/abort" ]; then
      # Add ability to abort test run between quickstart runs
      echo "${basedir}/abort file found. Exiting"
      exit 1
    fi
  fi
done


end=$SECONDS
duration=$((end - start))
echo "All tests run in $(($duration / 60))m$(($duration % 60))s."

test_status=0
if [ -z "${failed_tests}" ]; then
  echo "${GREEN_CONSOLE}All tests passed!${NOCOLOUR_CONSOLE}"
else
  echo "${RED_CONSOLE}There were test failures. See the logs for details. The failed tests were: ${failed_tests} ${NOCOLOUR_CONSOLE}"
  test_status=1
fi

if [ -f "${basedir}/enable-wait" ]; then
  # If we find the emable-wait marker file (used above for debugging),
  # fail the test run so we don't accidentally merge this file
  exit 1
fi

exit ${test_status}