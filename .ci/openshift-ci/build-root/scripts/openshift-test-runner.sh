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
  echo "Waiting $seconds. 'oc rsh' in and either 'touch continue' to stop waiting, or 'touch exit' to abort the test run. The latter will result in the test being reported as failed"
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

for file in ${basedir}/*; do
  fileName=$(basename "${file}")
  if [ ! -d "${file}" ]; then
    # echo "${fileName} is not a directory!"
    continue
  fi

  # Quickstarts that have not been migrated yet
  # TODO once everything has a quickstart_xxx_ci.yml file we can remove the included-directories check
  grep -q "^${fileName}$" included-directories.txt
  is_in_included_txt="$?"
  if [ "${is_in_included_txt}" != "0" ] && [ ! -f "${basedir}/.github/workflows/quickstart_${fileName}_ci.yml" ]; then
    # echo "Skipping ${fileName}!"
    continue
  fi

  #echo "${fileName}"
  runQuickstart "${script_directory}" "${fileName}"
done

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