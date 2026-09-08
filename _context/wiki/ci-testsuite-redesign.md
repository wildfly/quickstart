# WildFly Quickstarts — CI Testsuite Redesign

Design specification for the WildFly Quickstarts CI Testsuite

---

## 1. Goals

- Any quickstart can be tested **locally** with a single command, with no knowledge of GitHub Actions.
- The same scripts run identically in GitHub Actions — the workflow is a thin orchestration wrapper, not a test definition.
- A **single GitHub workflow file** replaces ~55 per-quickstart workflow files and the old `project_ci.yml`.
- WildFly is built **once** per PR run; the Maven repository is **cached and shared** across all matrix jobs.
- Only quickstarts **affected by the PR** are tested in GitHub Actions; locally the user decides scope.
- Each quickstart owns its test configuration or logic — no central registry to keep in sync.

---

## 2. Repository layout

```
wildfly-quickstarts/
├── .ci/
│   └── test-quickstarts.sh                 ← project runner
│
├── helloworld/
│   └── .ci/
│       └── test-quickstart.env              ← per-quickstart env (mandatory to opt-in for Quickstarts to be tested using common logic)
│
├── microprofile-reactive-messaging-kafka/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← starts Kafka container
│       └── after-test-quickstart.sh        ← stops Kafka container
│
├── micrometer/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← docker compose up -d
│       └── after-test-quickstart.sh        ← docker compose down
│
├── opentelemetry-tracing/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← docker compose up -d
│       └── after-test-quickstart.sh        ← docker compose down
│
├── todo-backend/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← starts Postgres container
│       └── after-test-quickstart.sh        ← stops Postgres container
│
├── mail/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← docker compose up -d (Greenmail)
│       └── after-test-quickstart.sh        ← docker compose down
│
├── remote-helloworld-mdb/
│   └── .ci/
│       ├── test-quickstart.env
│       ├── before-test-quickstart.sh       ← starts ActiveMQ Artemis container
│       └── after-test-quickstart.sh        ← stops ActiveMQ Artemis container
│
├── ejb-txn-remote-call/
│   └── .ci/
│       ├── test-quickstart.sh              ← fully standalone (no delegation, mandatory to opt-in for Quickstarts that can't be tested using the common logic)
│       ├── before-test-quickstart.sh       ← starts Postgres container
│       └── after-test-quickstart.sh        ← stops Postgres container
│
└── .github/workflows/
    └── quickstart_ci.yml                   ← THE single workflow
```

A quickstart is **opted in to CI** if and only if it has a `<qs>/.ci/test-quickstart.env` or a `<qs>/.ci/test-quickstart.sh` file.
Quickstarts without one of those files are ignored by both the project runner and the GitHub workflow.

---

## 3. Per-quickstart `.ci/` convention

### 3.1 Files

| File | Required | Purpose |
|---|---|---|
| `.ci/test-quickstart.env` | Yes for common quickstarts | Key=value file (no `export`, no shell logic) sourced by the project runner to load `QS_*` overrides. May be empty for quickstarts that use all defaults. |
| `.ci/test-quickstart.sh` | Yes for non common quickstarts | Full self-contained test script (e.g. `ejb-txn-remote-call`). When present, the project runner executes it directly and skips the common flow entirely. |
| `.ci/before-test-quickstart.sh` | No | Run before any Maven step. Used to start Docker services (Kafka, Postgres, Greenmail, Artemis). |
| `.ci/after-test-quickstart.sh` | No | Run at exit, even on failure (via `trap`). Stops Docker services started by the before-script. |

A quickstart is **testable** if it has either `.ci/test-quickstart.env` or `.ci/test-quickstart.sh`.
Quickstarts that implement common flow use `.ci/test-quickstart.env` exclusively.
Quickstarts with fully custom test logic use `.ci/test-quickstart.sh` exclusively.
Having both in the same quickstart is not valid.

### 3.2 `QS_*` metadata variables

These are optionally set in `.ci/test-quickstart.env`.
The project runner sources the file (with `set -a`) to load overrides into the current shell.
The GitHub workflow setup job also sources the file in a subshell to read `QS_LINUX_ONLY`.

| Variable | Default | Description |
|---|---|---|
| `QS_TEST_PROVISIONED_SERVER` | auto-detected | `true` if `<QS_DEPLOYMENT_DIR>/pom.xml` contains a `provisioned-server` profile. Can be explicitly overridden to `false` to disable even when the profile exists. |
| `QS_TEST_BOOTABLE_JAR` | auto-detected | `true` if `<QS_DEPLOYMENT_DIR>/pom.xml` contains a `bootable-jar` profile. Can be explicitly overridden to `false`. |
| `QS_TEST_OPENSHIFT` | auto-detected | `true` if `<QS_DEPLOYMENT_DIR>/pom.xml` contains an `openshift` profile. Can be explicitly overridden to `false`. |
| `QS_LINUX_ONLY` | `false` | Restrict GitHub matrix to `ubuntu-latest` only. Set `true` for quickstarts that require Docker or have known Windows issues. |
| `QS_MVN_COMMAND` | `package` | Maven lifecycle goal. EAR-based quickstarts need `install`. |
| `QS_DEPLOYMENT_DIR` | `.` | Path to the sub-module containing the deployable artifact, relative to the quickstart root (e.g. `ear`, `webapp`). Also the pom.xml scanned for profile auto-detection. |
| `QS_EXTRA_RUN_ARGS` | _(empty)_ | Extra `-D` arguments forwarded to `wildfly:start` / `wildfly:start-jar`. |

**Profile auto-detection** is performed by the project runner (not the per-quickstart `.ci/test-quickstart.env`).
When `QS_TEST_*` is not set in `test-quickstart.env`, the runner scans
`<qs>/<QS_DEPLOYMENT_DIR>/pom.xml` for the presence of the relevant `<profile><id>` element.
A value in `test-quickstart.env` always takes precedence over auto-detection.

### 3.3 Common-case template

A quickstart that uses all defaults has an empty `test-quickstart.env`:

```
# helloworld/.ci/test-quickstart.env
# (empty — all QS_* variables use their defaults)
```

### 3.4 Linux-only testing (docker run)

```
# microprofile-reactive-messaging-kafka/.ci/test-quickstart.env
QS_LINUX_ONLY=true
```

```bash
#!/usr/bin/env bash
# microprofile-reactive-messaging-kafka/.ci/before-test-quickstart.sh
docker run --rm -d --name "microprofile-reactive-messaging-kafka-kafka" -p 9092:9092 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 quay.io/ogunalp/kafka-native:0.5.0-kafka-3.6.0
```

```bash
#!/usr/bin/env bash
# microprofile-reactive-messaging-kafka/.ci/after-test-quickstart.sh
docker stop microprofile-reactive-messaging-kafka-kafka
```

### 3.5 Custom Maven build command

```
# ejb-throws-exception/.ci/test-quickstart.env
QS_MVN_COMMAND=install
QS_DEPLOYMENT_DIR=ear
```

### 3.6 Disabling a detected profile

```
# <quickstart>/.ci/test-quickstart.env
# provisioned-server and/or openshift profiles exist in pom.xml but are not functional
QS_TEST_PROVISIONED_SERVER=false
QS_TEST_OPENSHIFT=false
```

### 3.7 docker compose before/after pattern

Quickstarts that use `docker-compose.yml` in their root (e.g. `micrometer`, `opentelemetry-tracing`, `mail`) use the simplest possible before/after scripts:

```bash
#!/usr/bin/env bash
# <quickstart>/.ci/before-test-quickstart.sh
docker compose up -d
```

```bash
#!/usr/bin/env bash
# <quickstart>/.ci/after-test-quickstart.sh
docker compose down
```

The before-script runs from the quickstart directory (the runner `cd`s there before calling it), so `docker compose` picks up the `docker-compose.yml` at the root automatically.

### 3.8 Fully standalone (ejb-txn-remote-call)

This quickstart starts **three separate WildFly instances** and cannot use the generic provisioned-server flow. It has a `.ci/test-quickstart.sh` (no `.ci/test-quickstart.env`) which the project runner executes directly. It has its own before/after scripts for the Postgres database it requires:

```bash
#!/usr/bin/env bash
# ejb-txn-remote-call/.ci/before-test-quickstart.sh
docker run -d --rm --name "ejb-txn-remote-call-db" -p 5432:5432 -e POSTGRES_DB=test -e POSTGRES_USER=test -e POSTGRES_PASSWORD=test postgres:9.4 -c max-prepared-transactions=110 -c log-statement=all
```

```bash
#!/usr/bin/env bash
# ejb-txn-remote-call/.ci/after-test-quickstart.sh
docker stop ejb-txn-remote-call-db
```

The standalone `test-quickstart.sh` calls these scripts directly (they are not invoked by the project runner for this quickstart).

> **Rule for standalone scripts:** `QS_LINUX_ONLY` **must be declared as a bare `export` statement
> at the very top** of the standalone script, before `set -euo pipefail` or any executable code.
> The GitHub matrix setup job reads this value with `grep -q "^export QS_LINUX_ONLY=true"` —
> sourcing the script is unsafe because it runs real server/Docker commands.
>
> ```bash
> #!/usr/bin/env bash
> export QS_LINUX_ONLY=true   # ← must be line 1 (after shebang), before set -euo pipefail
> set -euo pipefail
> ...
> ```

---

## 4. Project runner — `.ci/test-quickstarts.sh`

### 4.1 Modes of operation

| Invocation | Behaviour |
|---|---|
| `.ci/test-quickstarts.sh` | Discover all testable quickstarts (have `.ci/test-quickstart.env` or `.ci/test-quickstart.sh`) and run each in alphabetical order. |
| `.ci/test-quickstarts.sh -q <name>` | Run a single named quickstart. This is the recommended way to test any quickstart. |
| `.ci/test-quickstarts.sh -r <name>` | Resume: run all testable quickstarts in alphabetical order, starting from `<name>` (inclusive). Useful for continuing a local run after a failure without re-testing quickstarts that already passed. |
| `.ci/test-quickstarts.sh --version-server <ver>` | Override `version.server` Maven property (WildFly snapshot builds). Combinable with `-q` or `-r`. |

When invoked without `-q` or `-r`, the project runner discovers all testable quickstarts by
globbing both `*/.ci/test-quickstart.env` and `*/.ci/test-quickstart.sh` from the repo root, deduplicating,
and running each in alphabetical order. The `-r` flag skips all quickstarts that sort before
`<name>` in that same order.

### 4.2 Profile auto-detection

When `QS_TEST_PROVISIONED_SERVER`, `QS_TEST_BOOTABLE_JAR`, or `QS_TEST_OPENSHIFT` are not
set by the per-quickstart script, the runner scans `<qs>/<QS_DEPLOYMENT_DIR>/pom.xml`:

```bash
has_profile() {
  local pom="$1" profile_id="$2"
  grep -q "<id>${profile_id}</id>" "${pom}"
}
```

Values set in `.ci/test-quickstart.env` always override auto-detection.

### 4.3 Single-quickstart execution flow

```
1. cd <qs>/
2. If .ci/test-quickstart.sh exists → execute it with bash and return (standalone path)
3. Source <qs>/.ci/test-quickstart.env (set -a) → load any QS_* overrides
4. Auto-detect any QS_TEST_* not explicitly set (scan deployment pom.xml)
5. If .ci/before-test-quickstart.sh exists → run it
6. Register .ci/after-test-quickstart.sh in trap EXIT (runs even on failure);
   cleanup also restores cwd to REPO_ROOT
7. mvn -fae clean ${QS_MVN_COMMAND} -Drelease [version.server]
8. If QS_TEST_PROVISIONED_SERVER=true:
     optional: ${QS_DEPLOYMENT_DIR}/target/server/bin/add-user.sh (if present)
     wildfly:start → mvn verify -Pintegration-testing → wildfly:shutdown
9. If QS_TEST_BOOTABLE_JAR=true:
     wildfly:start-jar → mvn verify -Pintegration-testing → wildfly:shutdown
10. If QS_TEST_OPENSHIFT=true:
     mvn -fae clean ${QS_MVN_COMMAND} -Popenshift [version.server]
11. Remove trap, run cleanup explicitly (after-script + cd REPO_ROOT)
```

Note: the `cd <qs>/` happens first (step 1), before sourcing `test-quickstart.env`. This means the
before-script path is resolved as `./.ci/before-test-quickstart.sh` (relative), and
`docker compose` in before/after scripts correctly picks up the quickstart's own
`docker-compose.yml`.

### 4.4 Changed-only detection

The project runner does **not** implement changed-only detection.
That logic lives exclusively in the GitHub workflow's setup job (§5.3).
Locally, the user runs either all quickstarts or a single one via `.ci/test-quickstarts.sh -q <name>`.

---

## 5. Single GitHub Actions workflow

**File:** `.github/workflows/quickstart_ci.yml` — workflow name: **`Quickstarts CI`**

### 5.1 Trigger

```yaml
on:
  pull_request:
    types: [opened, synchronize, reopened, ready_for_review]
    # No paths filter — all PRs trigger; the Setup job decides what actually runs
```

### 5.2 Job graph

```
PR opened / updated
        │
        ▼
  ┌──────────────────────┐
  │   WildFly-build      │
  │ (shared workflow,    │
  │  always attempted;   │
  │  silently skipped if │
  │  branch not found    │
  │  in wildfly/wildfly) │
  └──────────┬───────────┘
             │ wildfly-version output
             │ (empty if skipped)
             ├─────────────────────────────────────┐
             ▼                                     ▼
  ┌───────────┐                        ┌──────────────────────┐
  │  Setup    │                        │  Project-default     │
  │ (ubuntu)  │                        │  (matrix: jdk × os)  │
  │           │                        │  no needs            │
  │ • detect  │                        │  always runs         │
  │   changed │                        └──────────────────────┘
  │   QS list │
  │ • source  │                        ┌──────────────────────┐
  │   QS_vars │                        │  Project-with-deps   │
  │ • emit    │                        │  (matrix: jdk × os)  │
  │   matrices│                        │  needs: WildFly-build│
  └─────┬─────┘                        │  if: version != ''   │
        │ two matrix outputs           └──────────────────────┘
        │
        ├──────────────────────┐
        ▼                      ▼
  ┌────────────────────┐  ┌───────────────────────┐
  │  Quickstart-default│  │  Quickstart-with-deps  │
  │  (matrix:          │  │  (matrix:              │
  │   qs × os × jdk)   │  │   qs × os × jdk)       │
  │  needs: [Setup]    │  │  needs: [Setup,        │
  │                    │  │   WildFly-build]        │
  │  VERSION_SERVER="" │  │  if: with-deps-matrix  │
  │                    │  │      is not empty       │
  │  always runs when  │  │                        │
  │  matrix non-empty  │  │  VERSION_SERVER=       │
  │                    │  │  <snapshot>            │
  └────────────────────┘  └───────────────────────┘
```

### 5.3 Setup job — changed-quickstart detection

The setup job determines which quickstarts to test and emits a flat JSON matrix of
`(qs, os, jdk)` triples.

**Shared infrastructure directories** — when any file under these paths changes,
all testable quickstarts are run:

```
shared-doc/
.ci/
pom.xml
.github/workflows/quickstart_ci.yml
```

**Detection logic (pseudocode):**

```bash
# WILDFLY_VERSION comes from needs.WildFly-build.outputs.wildfly-version
BASE="${{ github.base_ref }}"
changed=$(git diff --name-only origin/${BASE}...HEAD)

# Check if shared infra changed → run all testable quickstarts
run_all=false
for dir in shared-doc .ci pom.xml .github/workflows/quickstart_ci.yml; do
  echo "$changed" | grep -q "^${dir}" && run_all=true && break
done

# Collect testable quickstarts (have .ci/test-quickstart.env OR .ci/test-quickstart.sh),
# deduplicated and sorted
all_qs=()
for marker in */.ci/test-quickstart.env */.ci/test-quickstart.sh; do
  [[ -f "${marker}" ]] || continue
  qs="$(dirname "$(dirname "${marker}")")"
  all_qs+=("${qs}")
done
IFS=$'\n' all_qs=($(printf '%s\n' "${all_qs[@]}" | sort -u)); unset IFS

# Filter to changed ones unless run_all=true
# For each qualifying QS: read QS_LINUX_ONLY
#   .env quickstarts  → source test-quickstart.env in a subshell
#   standalone .sh    → grep -q "^export QS_LINUX_ONLY=true"

# Build two matrix arrays: one entry per (qs, os, jdk) triple
JDKS=(17 25)
default_entries=()
with_deps_entries=()
for qs in "${to_test[@]}"; do
  for jdk in "${JDKS[@]}"; do
    default_entries+=("{\"qs\":\"${qs}\",\"os\":\"ubuntu-latest\",\"jdk\":${jdk}}")
    [ -n "${WILDFLY_VERSION}" ] && \
      with_deps_entries+=("{\"qs\":\"${qs}\",\"os\":\"ubuntu-latest\",\"jdk\":${jdk}}")
    if [ "${QS_LINUX_ONLY}" != "true" ]; then
      default_entries+=("{\"qs\":\"${qs}\",\"os\":\"windows-latest\",\"jdk\":${jdk}}")
      [ -n "${WILDFLY_VERSION}" ] && \
        with_deps_entries+=("{\"qs\":\"${qs}\",\"os\":\"windows-latest\",\"jdk\":${jdk}}")
    fi
  done
done

echo "default-matrix=[$(join , "${default_entries[@]}")]"    >> $GITHUB_OUTPUT
echo "with-deps-matrix=[$(join , "${with_deps_entries[@]}")]" >> $GITHUB_OUTPUT
echo "wildfly-version=${WILDFLY_VERSION}"                     >> $GITHUB_OUTPUT
```

> **Empty matrix:** if no quickstarts are in scope (e.g. a docs-only PR), GitHub Actions
> silently skips both Quickstart jobs. No special handling needed.

### 5.4 WildFly-build job

```yaml
WildFly-build:
  uses: wildfly/wildfly/.github/workflows/shared-wildfly-build.yml@main
  with:
    wildfly-branch: ${{ github.base_ref }}
    wildfly-repo: "wildfly/wildfly"
  # Behaviour when the branch does not exist in wildfly/wildfly:
  # the reusable workflow silently skips — the job is reported as skipped,
  # not failed. Downstream jobs that `needs: WildFly-build` still run;
  # the `wildfly-version` output is empty.
```

Every PR targeting **any** branch triggers this job. The two test legs are:

- **Default** (always runs): no `VERSION_SERVER` → each quickstart tests against the released version.
- **With-deps** (conditional): only runs if `wildfly-version` output is non-empty, i.e. the branch exists in `wildfly/wildfly`. Tests run with `-Dversion.server=<snapshot>`.

### 5.5 Setup job matrix outputs

The Setup job emits **two separate matrix outputs** — one per leg — both as JSON arrays of
`(qs, os, jdk)` triples. The with-deps matrix is an empty array `[]` when `WildFly-build`
did not produce a version.

### 5.6 Quickstart-default job

```yaml
Quickstart-default:
  name: "${{ matrix.qs }} — JDK ${{ matrix.jdk }} — ${{ matrix.os }}"
  runs-on: ${{ matrix.os }}
  needs: [Setup]
  if: needs.Setup.outputs.default-matrix != '[]'
  strategy:
    fail-fast: false
    matrix:
      include: ${{ fromJSON(needs.Setup.outputs.default-matrix) }}
  steps:
    - uses: actions/checkout@v4
    - name: Set up JDK ${{ matrix.jdk }}
      uses: actions/setup-java@v4
      with:
        java-version: ${{ matrix.jdk }}
        distribution: temurin
        cache: maven
    - name: Run quickstart tests
      run: .ci/test-quickstarts.sh -q ${{ matrix.qs }}
      shell: bash
    - uses: actions/upload-artifact@v4
      if: failure()
      with:
        name: surefire-${{ matrix.qs }}-JDK${{ matrix.jdk }}-${{ matrix.os }}
        path: "${{ matrix.qs }}/**/surefire-reports/*.txt"
```

### 5.7 Quickstart-with-deps job

```yaml
Quickstart-with-deps:
  name: "${{ matrix.qs }} — JDK ${{ matrix.jdk }} — ${{ matrix.os }} — ${{ needs.Setup.outputs.wildfly-version }}"
  runs-on: ${{ matrix.os }}
  needs: [Setup, WildFly-build]
  if: needs.Setup.outputs.with-deps-matrix != '[]'
  strategy:
    fail-fast: false
    matrix:
      include: ${{ fromJSON(needs.Setup.outputs.with-deps-matrix) }}
  steps:
    - uses: actions/checkout@v4
    - name: Set up JDK ${{ matrix.jdk }}
      uses: actions/setup-java@v4
      with:
        java-version: ${{ matrix.jdk }}
        distribution: temurin
        cache: maven
    - name: Download WildFly Maven repository
      uses: actions/download-artifact@v4
      with:
        name: wildfly-maven-repository
        path: .
    - name: Extract WildFly Maven repository
      run: tar -xzf wildfly-maven-repository.tar.gz -C ~
      shell: bash
    - name: Run quickstart tests
      env:
        VERSION_SERVER: ${{ needs.Setup.outputs.wildfly-version }}
      run: .ci/test-quickstarts.sh -q ${{ matrix.qs }}
      shell: bash
    - uses: actions/upload-artifact@v4
      if: failure()
      with:
        name: surefire-${{ matrix.qs }}-JDK${{ matrix.jdk }}-${{ matrix.os }}-${{ needs.Setup.outputs.wildfly-version }}
        path: "${{ matrix.qs }}/**/surefire-reports/*.txt"
```

### 5.8 Project-default job

```yaml
Project-default:
  name: "Project: JDK ${{ matrix.jdk }} — ${{ matrix.os }}"
  runs-on: ${{ matrix.os }}
  strategy:
    fail-fast: false
    matrix:
      jdk: [17, 25]
      os: [ubuntu-latest, windows-latest]
  steps:
    - uses: actions/checkout@v4
    - name: Set up JDK ${{ matrix.jdk }}
      uses: actions/setup-java@v4
      with:
        java-version: ${{ matrix.jdk }}
        distribution: temurin
        cache: maven
    - name: Build project release
      run: mvn -U -B -fae clean install -Drelease -P-provisioned-server,-bootable-jar
      shell: bash
    - uses: actions/upload-artifact@v4
      if: failure()
      with:
        name: surefire-project-JDK${{ matrix.jdk }}-${{ matrix.os }}
        path: "**/surefire-reports/*.txt"
```

### 5.9 Project-with-deps job

```yaml
Project-with-deps:
  name: "Project: JDK ${{ matrix.jdk }} — ${{ matrix.os }} — ${{ needs.WildFly-build.outputs.wildfly-version }}"
  runs-on: ${{ matrix.os }}
  needs: [WildFly-build]
  if: needs.WildFly-build.outputs.wildfly-version != ''
  strategy:
    fail-fast: false
    matrix:
      jdk: [17, 25]
      os: [ubuntu-latest, windows-latest]
  steps:
    - uses: actions/checkout@v4
    - name: Set up JDK ${{ matrix.jdk }}
      uses: actions/setup-java@v4
      with:
        java-version: ${{ matrix.jdk }}
        distribution: temurin
        cache: maven
    - name: Download WildFly Maven repository
      uses: actions/download-artifact@v4
      with:
        name: wildfly-maven-repository
        path: .
    - name: Extract WildFly Maven repository
      run: tar -xzf wildfly-maven-repository.tar.gz -C ~
      shell: bash
    - name: Build project release with snapshot server
      run: mvn -U -B -fae clean install -Drelease -P-provisioned-server,-bootable-jar -Dversion.server=${{ needs.WildFly-build.outputs.wildfly-version }}
      shell: bash
    - uses: actions/upload-artifact@v4
      if: failure()
      with:
        name: surefire-project-JDK${{ matrix.jdk }}-${{ matrix.os }}-${{ needs.WildFly-build.outputs.wildfly-version }}
        path: "**/surefire-reports/*.txt"
```

---

## 6. Matrix construction

### 6.1 Flat triple approach

The setup job emits **two separate** JSON arrays of `(quickstart, os, jdk)` triples —
one per leg — consumed by `Quickstart-default` and `Quickstart-with-deps` respectively. Each combination
becomes an independent job on the correct runner OS. Each matrix stays within the 256-job limit.

| Quickstart | QS_LINUX_ONLY | Quickstart-default jobs (2 JDKs) | Quickstart-with-deps jobs (when branch exists) |
|---|---|---|---|
| helloworld | false | ubuntu/17, ubuntu/25, windows/17, windows/25 → **4** | same → **4** |
| microprofile-reactive-messaging-kafka | true | ubuntu/17, ubuntu/25 → **2** | same → **2** |
| micrometer | false | ubuntu/17, ubuntu/25, windows/17, windows/25 → **4** | same → **4** |
| opentelemetry-tracing | false | ubuntu/17, ubuntu/25, windows/17, windows/25 → **4** | same → **4** |
| mail | true | ubuntu/17, ubuntu/25 → **2** | same → **2** |
| todo-backend | true | ubuntu/17, ubuntu/25 → **2** | same → **2** |
| ejb-txn-remote-call | true | ubuntu/17, ubuntu/25 → **2** | same → **2** |

Maximum per matrix when all ~55 quickstarts are in scope:
55 × 2 JDKs × ~1.5 avg OS ≈ **165 jobs** per matrix — well within the 256-job limit.

### 6.2 Default and with-deps legs

The two legs run as separate jobs in the same workflow run:

- **`Quickstart-default`** — always runs when the matrix is non-empty; no `VERSION_SERVER`.
- **`Quickstart-with-deps`** — only runs when `WildFly-build` produced a `wildfly-version` output; `VERSION_SERVER` set to that version. Skipped entirely when the branch does not exist in `wildfly/wildfly`.

Both jobs share the same step structure; `Quickstart-default` sets no `VERSION_SERVER` and skips the
Maven repository download, while `Quickstart-with-deps` always sets `VERSION_SERVER` and always
downloads the repository (it only runs when the artifact exists).

The `Project-default` and `Project-with-deps` jobs follow the same default/with-deps split but
run a full `mvn clean install -Drelease` across the whole project tree, skipping
`-P-provisioned-server,-bootable-jar` (those profiles are exercised per-quickstart by the
Quickstart-* jobs).

---

## 7. Resource sharing strategy

| Resource | How shared |
|---|---|
| WildFly build | Single `WildFly-build` job; `wildfly-version` output consumed by all with-deps jobs via `needs.` expression. Maven repo tarball downloaded per Test job via `actions/download-artifact`. |
| Maven repository cache | `actions/setup-java` with `cache: maven` — keyed on `pom.xml` hash, shared across all matrix jobs on the same OS. Significantly reduces download time for the WildFly dependency set. |
| Checkout | Each job checks out the repo independently (required for parallel runners). |
| Surefire reports | Uploaded per failing job with a name scoped to avoid artifact name collisions. |

---

## 8. Migration map

| Current | Action | Replacement |
|---|---|---|
| `.ci/test-quickstart.sh` (project runner) | **renamed** | `.ci/test-quickstarts.sh` |
| `.github/workflows/quickstart_ci.yml` | **rewritten** | Single workflow as described in §5 |
| `.github/workflows/project_ci.yml` | **deleted** | `Project-default` / `Project-with-deps` jobs in `quickstart_ci.yml` |
| `.github/workflows/quickstart_<qs>_ci.yml` (~55 files) | **deleted** | Per-quickstart `<qs>/.ci/test-quickstart.env` or `<qs>/.ci/test-quickstart.sh` |
| `.github/workflows/quickstart_<qs>_ci_before.sh` | **deleted** | `<qs>/.ci/before-test-quickstart.sh` + `<qs>/.ci/after-test-quickstart.sh` |
| `ejb-txn-remote-call` inline workflow logic | **migrated** | `ejb-txn-remote-call/.ci/test-quickstart.sh` (standalone) + separate before/after scripts |
| `.github/workflows/kubernetes-ci.yml` | **updated** | Opt-in check replaced: presence of `<qs>/.ci/test-quickstart.env` or `<qs>/.ci/test-quickstart.sh` instead of `quickstart_${qs}_ci.yml` |

### 8.1 Before/after-script migration

All before-scripts now have a matching after-script that tears down what was started.

| Quickstart | before-test-quickstart.sh | after-test-quickstart.sh |
|---|---|---|
| `microprofile-reactive-messaging-kafka` | starts Kafka container (`docker run`) | `docker stop microprofile-reactive-messaging-kafka-kafka` |
| `micrometer` | `docker compose up -d` | `docker compose down` |
| `opentelemetry-tracing` | `docker compose up -d` | `docker compose down` |
| `mail` | `docker compose up -d` (Greenmail) | `docker compose down` |
| `remote-helloworld-mdb` | starts Artemis container (`docker run`) | `docker stop artemis` |
| `todo-backend` | starts Postgres container (`docker run`) | `docker stop todo-backend-db` |
| `ejb-txn-remote-call` | starts Postgres container (`docker run`) | `docker stop ejb-txn-remote-call-db` |

---

## 9. Files kept unchanged

The following GitHub workflow files are **out of scope** and are not touched:

- `.github/workflows/publish-pages.yml`
- `.github/workflows/reduce_readme.yml`

`.github/workflows/kubernetes-ci.yml` was updated as part of this redesign — see §8.

---

## 10. Local usage examples

```bash
# Run all testable quickstarts
.ci/test-quickstarts.sh

# Run a single quickstart via the project runner
.ci/test-quickstarts.sh -q helloworld

# Resume all quickstarts from kitchensink onwards (e.g. after a failure at kitchensink)
.ci/test-quickstarts.sh -r kitchensink

# Test against a locally built WildFly snapshot
.ci/test-quickstarts.sh -q microprofile-health \
    --version-server 36.0.0.Beta1-SNAPSHOT

# Resume from a specific quickstart with a snapshot version
.ci/test-quickstarts.sh -r microprofile-config \
    --version-server 36.0.0.Beta1-SNAPSHOT

# Kafka quickstart — Docker must already be running
# (before-test-quickstart.sh starts Kafka automatically when called via the runner)
microprofile-reactive-messaging-kafka/.ci/test-quickstart.sh
```
