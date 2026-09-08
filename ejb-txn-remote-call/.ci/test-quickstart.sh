#!/usr/bin/env bash
# Standalone — does NOT delegate to the project runner.
# This quickstart starts three separate WildFly instances and cannot
# use the generic provisioned-server flow.

# QS_LINUX_ONLY is read by the GitHub matrix setup job before any other code runs
export QS_LINUX_ONLY=true

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# Quickstart root is always one directory above this script's .ci/ directory
QUICKSTART_ROOT="$SCRIPT_DIR/.."
cd "${QUICKSTART_ROOT}"

log() { echo "[ejb-txn-remote-call] $*"; }

version_server_arg=""
[[ -n "${VERSION_SERVER:-}" ]] && version_server_arg="-Dversion.server=${VERSION_SERVER}"

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
# Before hook — run directly when this script is invoked standalone;
# the project runner also calls it, but running it twice is harmless since
# the container is started with --rm and a fixed name would conflict, so we
# only run it here if the runner has not already done so.
# ---------------------------------------------------------------------------
BEFORE_SCRIPT="$SCRIPT_DIR/before-test-quickstart.sh"
if [[ -f "${BEFORE_SCRIPT}" ]]; then
  log "Running before-test-quickstart.sh..."
  bash "${BEFORE_SCRIPT}"
fi

# ---------------------------------------------------------------------------
# After hook (Postgres container) — runs on exit even on failure
# ---------------------------------------------------------------------------
AFTER_SCRIPT="$SCRIPT_DIR/after-test-quickstart.sh"
after_hook() {
  if [[ -f "${AFTER_SCRIPT}" ]]; then
    log "Running after-test-quickstart.sh..."
    bash "${AFTER_SCRIPT}" || true
  fi
}
trap after_hook EXIT

# ---------------------------------------------------------------------------
# Helper: shut down the three provisioned WildFly servers
# ---------------------------------------------------------------------------
shutdown_servers() {
  log "Shutting down client (server1)..."
  # shellcheck disable=SC2086
  (cd "${QUICKSTART_ROOT}/client" && mvn wildfly:shutdown ${version_server_arg}) || true
  log "Shutting down server2 (port 10090)..."
  # shellcheck disable=SC2086
  (cd "${QUICKSTART_ROOT}/server" && mvn wildfly:shutdown -Dwildfly.port=10090 ${version_server_arg}) || true
  log "Shutting down server3 (port 10190)..."
  # shellcheck disable=SC2086
  (cd "${QUICKSTART_ROOT}/server" && mvn wildfly:shutdown -Dwildfly.port=10190 ${version_server_arg}) || true
}

# ---------------------------------------------------------------------------
# Step 1 – Build for release
# ---------------------------------------------------------------------------
log "=== Step 1: Build for release ==="
# shellcheck disable=SC2086
mvn -fae clean package -Drelease ${version_server_arg}

# ---------------------------------------------------------------------------
# Step 2 – Provisioned-server: client + two server instances
# ---------------------------------------------------------------------------
log "=== Step 2: Build, run & test with provisioned-server profile ==="

cd client
log "Building 'client' provisioned server..."
# shellcheck disable=SC2086
mvn -fae clean package \
  -DremoteServerUsername="quickstartUser" \
  -DremoteServerPassword="quickstartPwd1!" \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  ${version_server_arg}

log "Starting 'client' provisioned server (server1)..."
# shellcheck disable=SC2086
mvn wildfly:start \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  -Dwildfly.javaOpts="-Djboss.tx.node.id=server1 -Djboss.node.name=server1" \
  -Dstartup-timeout=120 \
  ${version_server_arg}

cd ../server
log "Building 'server' provisioned server (server2 + server3)..."
# shellcheck disable=SC2086
mvn -fae clean package \
  -Dwildfly.provisioning.dir=server2 \
  -Djboss-as.home=target/server2 \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  ${version_server_arg}
# shellcheck disable=SC2086
mvn -fae package \
  -Dwildfly.provisioning.dir=server3 \
  -Djboss-as.home=target/server3 \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  ${version_server_arg}

log "Adding quickstartUser to server2 and server3..."
./target/server2/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!'
./target/server3/bin/add-user.sh -a -u 'quickstartUser' -p 'quickstartPwd1!'

log "Starting provisioned server2 (port-offset 100)..."
# shellcheck disable=SC2086
mvn wildfly:start \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  -Djboss-as.home=target/server2 \
  -Dwildfly.javaOpts="-Djboss.socket.binding.port-offset=100 -Djboss.tx.node.id=server2 -Djboss.node.name=server2" \
  -Dstartup-timeout=120 \
  ${version_server_arg}

log "Starting provisioned server3 (port-offset 200)..."
# shellcheck disable=SC2086
mvn wildfly:start \
  -DpostgresqlUsername="test" \
  -DpostgresqlPassword="test" \
  -Djboss-as.home=target/server3 \
  -Dwildfly.javaOpts="-Djboss.socket.binding.port-offset=200 -Djboss.tx.node.id=server3 -Djboss.node.name=server3" \
  -Dstartup-timeout=120 \
  ${version_server_arg}

log "Testing provisioned servers..."
cd ../client
# shellcheck disable=SC2086
mvn -fae verify -Pintegration-testing ${version_server_arg}
cd ../server
# shellcheck disable=SC2086
mvn -fae verify -Dserver.host="http://localhost:8180" -Pintegration-testing ${version_server_arg}
# shellcheck disable=SC2086
mvn -fae verify -Dserver.host="http://localhost:8280" -Pintegration-testing ${version_server_arg}

shutdown_servers

# ---------------------------------------------------------------------------
# Step 3 – OpenShift profile build (no servers needed)
# ---------------------------------------------------------------------------
log "=== Step 3: Build with openshift profile ==="
cd "${QUICKSTART_ROOT}/client"
# shellcheck disable=SC2086
mvn -fae clean package \
  -Popenshift \
  -DremoteServerUsername="quickstartUser" \
  -DremoteServerPassword="quickstartPwd1!" \
  ${version_server_arg}
cd ../server
# shellcheck disable=SC2086
mvn -fae clean package -Popenshift ${version_server_arg}

log "=== Completed: ejb-txn-remote-call ==="
