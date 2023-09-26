# The main script to run the quickstarts
# It iterates over all found quickstart directories, ignoring the ones found in
# excluded-directories.txt, and runs the run-quickstart-test-on-openshift-for each of them

RED_CONSOLE="\033[0;31m"
GREEN_CONSOLE="\033[0;32m"
NOCOLOUR_CONSOLE="\033[0m"

# Runs a single quickstart
#
# Parameters
# 1 - the directory containing this script
# 2 - the name of the quickstart directory
function runQuickstart() {
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

exit ${test_status}