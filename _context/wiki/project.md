# Project Overview

## What this project is

The **WildFly Quickstarts** repository is the official collection of ~60 reference applications demonstrating Jakarta EE and MicroProfile technologies running on the [WildFly application server](https://www.wildfly.org/). Each quickstart is a small, focused, working example that developers can use as a starting point or reference for their own projects.

The quickstarts cover a wide range of technologies: CDI, EJB, JPA, JSF, Jakarta REST, JMS, Security (Elytron), MicroProfile (Config, Health, Fault Tolerance, JWT, LRA, OpenAPI, Reactive Messaging, REST Client), OpenTelemetry, Micrometer, WebSockets, and more.

## Goals and priorities

1. **Modernisation** — Keep quickstarts aligned with the latest Jakarta EE and MicroProfile specifications and WildFly releases.
2. **OpenShift / Kubernetes compatibility** — Ensure quickstarts marked as OpenShift-compatible work reliably on container platforms; expand compatibility where feasible.
3. **Integration tests** — Improve test coverage and reliability across quickstarts.
4. **Documentation** — Improve clarity, completeness, and consistency of `README-source.adoc` files across all quickstarts.
5. **Cross-cutting improvements** — Prefer enhancements that can be applied uniformly to multiple quickstarts over one-off fixes.

## Ownership model

- **Project lead** owns the overall repository: maintenance, release procedures, cross-cutting enhancements, and coordination.
- **Each quickstart has an owner** — typically the team responsible for the main WildFly component the quickstart targets. Owners are identified in the quickstart's own `README-source.adoc`.
- Cross-cutting changes (shared docs, parent POM, CI, build tooling) are the project lead's direct responsibility.

## Repository structure

| Path | Purpose |
|------|---------|
| `<quickstart-name>/` | Each quickstart lives in its own top-level folder |
| `shared-doc/` | Shared AsciiDoc fragments included by quickstart READMEs |
| `pom.xml` | Parent Maven POM; defines common dependency management |
| `README-source.adoc` | Master README with the full quickstart table |
| `CODEOWNERS` | GitHub CODEOWNERS for per-quickstart ownership |
| `CONTRIBUTING.adoc` | Contribution guidelines |
| `RELEASE_PROCEDURE.adoc` | Release process documentation |
| `.github/` | CI workflows (GitHub Actions) |
| `.ci/` | Local CI scripts and configuration |
| `guide/` | Extended guide documentation |

## Key workflows

- **Adding a quickstart**: Create a new top-level folder, add `README-source.adoc`, wire into the parent `pom.xml`, and update `CODEOWNERS`. Maven profiles and sahred-docs includes should be reused. At least a basic integration test should be included, activated by Maven profile named "integration-testing"
- **Updating shared content**: Edit files under `shared-doc/` — changes propagate to all quickstarts that include those fragments.
- **Generating READMEs**: `README.adoc` / `README.html` files are generated from `README-source.adoc` using the Asciidoctor toolchain with attribute substitution, same for the `Table of Available Quickstarts` in the root `README`.
- **CI**: GitHub Actions runs builds and tests; OpenShift-compatible quickstarts are also tested on OpenShift.
- **Releases**: Follow `RELEASE_PROCEDURE.adoc`.

## OpenShift compatibility

Quickstarts flagged `Yes` in the *Openshift Compatible* column of the main table are expected to deploy and run on OpenShift without modification. Expanding this flag is an active goal.

## Technology tags (quick reference)

`CDI` · `EJB` · `JPA` · `JSF` · `Jakarta REST` · `JMS` · `MDB` · `Security / Elytron` · `MicroProfile` · `OpenTelemetry` · `Micrometer` · `WebSocket` · `Hibernate` · `Spring` · `Batch` · `JTA` · `Clustering`
