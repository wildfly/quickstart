#!/usr/bin/env bash
# =============================================================================
# test-quickstarts.sh  –  Run the integration test suite for WildFly quickstarts.
#
# USAGE
#   .ci/test-quickstarts.sh [OPTIONS]
#
# MODES
#   (no flags)                    Test all testable quickstarts in alphabetical order.
#   -q, --quickstart <name>       Test a single named quickstart.
#   -r, --resume <name>           Test all quickstarts in alphabetical order, starting
#                                 from <name> (inclusive). Useful after a local failure.
#
# OPTIONS
#   --version-server <version>    Pass -Dversion.server=<version> to every Maven call.
#                                 Combinable with -q or -r.
#   -h, --help                    Print this help and exit.
#
# ENVIRONMENT VARIABLES
#   VERSION_SERVER                Same as --version-server. CLI flag takes precedence.
#
# HOW IT WORKS
#   A quickstart is testable if it has EITHER:
#     - .ci/test-quickstart.env — common flow; file is sourced to read optional QS_* overrides.
#     - .ci/test-quickstart.sh  — fully standalone script (e.g. ejb-txn-remote-call);
#                                 executed directly with bash. No common flow is run.
#
#   Common flow (test-quickstart.env quickstarts):
#     1. Sources <qs>/.ci/test-quickstart.env (with set -a) to load any QS_* overrides.
#     2. Auto-detects QS_TEST_* flags not set explicitly (scans deployment pom.xml).
#     3. Runs <qs>/.ci/before-test-quickstart.sh if present.
#     4. Registers <qs>/.ci/after-test-quickstart.sh in a trap (runs even on failure).
#     5. Executes the Maven test phases enabled by the QS_* flags.
#
# EXAMPLES
#   .ci/test-quickstarts.sh
#   .ci/test-quickstarts.sh -q helloworld
#   .ci/test-quickstarts.sh -r kitchensink
#   .ci/test-quickstarts.sh -q microprofile-health --version-server 36.0.0.Beta1-SNAPSHOT
#   .ci/test-quickstarts.sh -r microprofile-config --version-server 36.0.0.Beta1-SNAPSHOT
# =============================================================================

set -euo pipefail

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
usage() {
  sed -n '/^# USAGE/,/^# =====/p' "$0" | sed '$d' | sed 's/^# \{0,3\}//'
  exit 0
}

die()  { echo "ERROR: $*" >&2; exit 1; }
log()  { echo "[test-quickstarts] $*"; }
info() { echo ""; log ">>> $*"; echo ""; }

# ---------------------------------------------------------------------------
# Resolve repo root from this script's location
# ---------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

# ---------------------------------------------------------------------------
# Ensure 'docker' is available as a real command in non-interactive subshells.
# On systems where 'docker' is only a shell alias (e.g. aliased to podman on
# macOS), child processes launched with 'bash script.sh' won't see it.
# If 'docker' is not a real binary but 'podman' is, we write a tiny shim into
# a temporary directory and prepend it to PATH so that all before/after scripts
# can use 'docker' transparently.
# ---------------------------------------------------------------------------
if ! command -v docker &>/dev/null; then
  if command -v podman &>/dev/null; then
    _shim_dir="$(mktemp -d)"
    printf '#!/usr/bin/env bash\nexec podman "$@"\n' > "${_shim_dir}/docker"
    chmod +x "${_shim_dir}/docker"
    export PATH="${_shim_dir}:${PATH}"
    log "docker not found as a binary — created podman shim at ${_shim_dir}/docker"
  else
    log "WARNING: neither 'docker' nor 'podman' found in PATH. Before/after scripts may fail."
  fi
fi

# ---------------------------------------------------------------------------
# Defaults
# ---------------------------------------------------------------------------
MODE=""          # "single" | "resume" | "all"
QS_NAME=""
RESUME_FROM=""
VERSION_SERVER="${VERSION_SERVER:-}"

# ---------------------------------------------------------------------------
# Parse CLI flags
# ---------------------------------------------------------------------------
while [[ $# -gt 0 ]]; do
  case "$1" in
    -q|--quickstart)      MODE=single;  QS_NAME="$2";     shift 2 ;;
    -r|--resume)          MODE=resume;  RESUME_FROM="$2"; shift 2 ;;
    --version-server)     VERSION_SERVER="$2";            shift 2 ;;
    -h|--help)            usage ;;
    *) die "Unknown option: $1" ;;
  esac
done

[[ -z "$MODE" ]] && MODE=all

# ---------------------------------------------------------------------------
# Build the optional -Dversion.server=... fragment
# ---------------------------------------------------------------------------
version_server_arg=""
[[ -n "${VERSION_SERVER}" ]] && version_server_arg="-Dversion.server=${VERSION_SERVER}"

# ---------------------------------------------------------------------------
# Profile auto-detection
# ---------------------------------------------------------------------------
has_profile() {
  local pom="$1" profile_id="$2"
  grep -q "<id>${profile_id}</id>" "${pom}" 2>/dev/null
}

# ---------------------------------------------------------------------------
# Run a single quickstart by name.
# Checks whether the quickstart's script is standalone (no delegation to this
# runner). Standalone scripts are exec'd directly; delegating scripts have
# their QS_* vars read and then the common flow is executed here.
# ---------------------------------------------------------------------------
run_quickstart() {
  local qs="$1"
  local qs_dir="${REPO_ROOT}/${qs}"
  local qs_script="${qs_dir}/.ci/test-quickstart.sh"
  local qs_env="${qs_dir}/.ci/test-quickstart.env"

  [[ -d "${qs_dir}" ]] || die "Quickstart directory not found: ${qs_dir}"
  [[ -f "${qs_script}" || -f "${qs_env}" ]] \
    || die "No .ci/test-quickstart.env or .ci/test-quickstart.sh found for: ${qs}"

  info "Testing quickstart: ${qs}"

  cd "${qs_dir}"

  # --- Standalone script: exec directly and return ---
  if [[ -f "${qs_script}" ]]; then
    log "Standalone script detected — executing directly: ${qs_script}"
    [[ -n "${VERSION_SERVER}" ]] && export VERSION_SERVER
    bash "${qs_script}"
    return
  fi

  # --- Common flow: source test-quickstart.env to load any QS_* overrides ---
  local QS_TEST_PROVISIONED_SERVER="" QS_TEST_BOOTABLE_JAR="" QS_TEST_OPENSHIFT=""
  local QS_LINUX_ONLY="false" QS_MVN_COMMAND="package" QS_DEPLOYMENT_DIR="." QS_EXTRA_RUN_ARGS=""

  set -a
  # shellcheck source=/dev/null
  source "${qs_env}"
  set +a

  local deployment_dir="${QS_DEPLOYMENT_DIR:-.}"
  local mvn_command="${QS_MVN_COMMAND:-package}"
  local extra_run_args="${QS_EXTRA_RUN_ARGS:-}"

  # Auto-detect QS_TEST_* from pom.xml when not explicitly set
  local pom="${qs_dir}/${deployment_dir}/pom.xml"
  local test_provisioned_server="${QS_TEST_PROVISIONED_SERVER}"
  local test_bootable_jar="${QS_TEST_BOOTABLE_JAR}"
  local test_openshift="${QS_TEST_OPENSHIFT}"

  if [[ -z "${test_provisioned_server}" ]]; then
    has_profile "${pom}" "provisioned-server" && test_provisioned_server="true" || test_provisioned_server="false"
  fi
  if [[ -z "${test_bootable_jar}" ]]; then
    has_profile "${pom}" "bootable-jar" && test_bootable_jar="true" || test_bootable_jar="false"
  fi
  if [[ -z "${test_openshift}" ]]; then
    has_profile "${pom}" "openshift" && test_openshift="true" || test_openshift="false"
  fi

  log "QS_DEPLOYMENT_DIR=${deployment_dir} MVN_COMMAND=${mvn_command} QS_EXTRA_RUN_ARGS=${extra_run_args}"
  log "TEST_PROVISIONED_SERVER=${test_provisioned_server} TEST_BOOTABLE_JAR=${test_bootable_jar} TEST_OPENSHIFT=${test_openshift}"

  # --- Before hook ---
  local before_script="./.ci/before-test-quickstart.sh"
  if [[ -f "${before_script}" ]]; then
    log "Running before-test-quickstart.sh..."
    bash "${before_script}"
  fi

  # --- After hook via trap ---
  local after_script="./.ci/after-test-quickstart.sh"
  _qs_cleanup() {
    if [[ -f "${after_script}" ]]; then
      log "Running after-test-quickstart.sh..."
      bash "${after_script}" || true
    fi
    cd "${REPO_ROOT}"
  }
  trap _qs_cleanup EXIT

  # --- Step 1: Build for release ---
  log "=== Step 1: Build for release ==="
  # shellcheck disable=SC2086
  mvn -fae clean "${mvn_command}" -Drelease ${version_server_arg}

  # --- Step 2: Provisioned-server ---
  if [[ "${test_provisioned_server}" == "true" ]]; then
    log "=== Step 2: Run & test with provisioned-server profile ==="
    local add_user="${deployment_dir}/target/server/bin/add-user.sh"
    if [[ -f "${add_user}" ]]; then
      log "Adding quickstartUser..."
      "${add_user}" -a -u 'quickstartUser' -p 'quickstartPwd1!' -g 'guest,user,JBossAdmin,Users'
      log "Adding quickstartAdmin..."
      "${add_user}" -a -u 'quickstartAdmin' -p 'adminPwd1!' -g 'guest,user,admin'
    fi
    log "Starting provisioned server..."
    # shellcheck disable=SC2086
    mvn -f "${deployment_dir}/pom.xml" wildfly:start -Dstartup-timeout=120 ${extra_run_args} ${version_server_arg}
    log "Testing provisioned server..."
    # shellcheck disable=SC2086
    mvn -fae verify -Pintegration-testing ${version_server_arg}
    log "Shutting down provisioned server..."
    # shellcheck disable=SC2086
    mvn -f "${deployment_dir}/pom.xml" wildfly:shutdown ${version_server_arg}
  fi

  # --- Step 3: Bootable jar ---
  if [[ "${test_bootable_jar}" == "true" ]]; then
    log "=== Step 3: Run & test with bootable-jar profile ==="
    log "Starting bootable jar..."
    # shellcheck disable=SC2086
    mvn -f "${deployment_dir}/pom.xml" wildfly:start-jar -Dstartup-timeout=120 ${extra_run_args} ${version_server_arg}
    log "Testing bootable jar..."
    # shellcheck disable=SC2086
    mvn -fae verify -Pintegration-testing ${version_server_arg}
    log "Shutting down bootable jar..."
    # shellcheck disable=SC2086
    mvn -f "${deployment_dir}/pom.xml" wildfly:shutdown ${version_server_arg}
  fi

  # --- Step 4: OpenShift profile build ---
  if [[ "${test_openshift}" == "true" ]]; then
    log "=== Step 4: Build with openshift profile ==="
    # shellcheck disable=SC2086
    mvn -fae clean "${mvn_command}" -Popenshift ${version_server_arg}
  fi

  # Remove trap now that we're done cleanly (cleanup still runs on error via trap)
  trap - EXIT
  _qs_cleanup

  log "=== Completed: ${qs} ==="
}

# ---------------------------------------------------------------------------
# Discover all testable quickstarts (alphabetical).
# A quickstart is testable if it has .ci/test-quickstart.env OR .ci/test-quickstart.sh.
# ---------------------------------------------------------------------------
discover_quickstarts() {
  local -a result=()
  for marker in "${REPO_ROOT}"/*/.ci/test-quickstart.env "${REPO_ROOT}"/*/.ci/test-quickstart.sh; do
    [[ -f "${marker}" ]] || continue
    local qs
    qs="$(basename "$(dirname "$(dirname "${marker}")")")"
    result+=("${qs}")
  done
  # Deduplicate and sort alphabetically
  IFS=$'\n' sorted=($(printf '%s\n' "${result[@]}" | sort -u)); unset IFS
  echo "${sorted[@]}"
}

# ---------------------------------------------------------------------------
# Main dispatch
# ---------------------------------------------------------------------------
case "${MODE}" in

  single)
    [[ -n "${QS_NAME}" ]] || die "-q requires a quickstart name."
    run_quickstart "${QS_NAME}"
    ;;

  resume)
    [[ -n "${RESUME_FROM}" ]] || die "-r requires a quickstart name."
    all=($(discover_quickstarts))
    [[ ${#all[@]} -gt 0 ]] || die "No testable quickstarts found."
    found=false
    failed=()
    for qs in "${all[@]}"; do
      if [[ "${qs}" == "${RESUME_FROM}" ]]; then found=true; fi
      [[ "${found}" == "true" ]] || continue
      set +e; ( set -euo pipefail; run_quickstart "${qs}" ); _rc=$?; set -e
      [[ ${_rc} -eq 0 ]] || failed+=("${qs}")
    done
    [[ "${found}" == "true" ]] || die "Quickstart '${RESUME_FROM}' not found. Available: ${all[*]}"
    if [[ ${#failed[@]} -gt 0 ]]; then
      log "FAILED quickstarts: ${failed[*]}"
      exit 1
    fi
    ;;

  all)
    all=($(discover_quickstarts))
    [[ ${#all[@]} -gt 0 ]] || { log "No testable quickstarts found."; exit 0; }
    log "Found ${#all[@]} testable quickstarts: ${all[*]}"
    failed=()
    for qs in "${all[@]}"; do
      set +e; ( set -euo pipefail; run_quickstart "${qs}" ); _rc=$?; set -e
      [[ ${_rc} -eq 0 ]] || failed+=("${qs}")
    done
    if [[ ${#failed[@]} -gt 0 ]]; then
      log "FAILED quickstarts: ${failed[*]}"
      exit 1
    fi
    ;;
esac

log "All done."
