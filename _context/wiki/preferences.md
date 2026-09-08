# Working Preferences

## AI Collaboration style

- **Always check existing patterns first** — Before suggesting any change, examine how similar quickstarts or shared components already handle the problem. Read the relevant `README-source.adoc`, `pom.xml`, and source files in comparable quickstarts.
- **Prefer cross-cutting solutions** — Favour approaches that can be applied consistently across multiple quickstarts over one-off fixes. If a change only makes sense for one quickstart, call that out explicitly.
- **Evolve patterns thoughtfully** — Existing patterns can and should be improved, but deviations must be deliberate and justified. Flag when a suggestion diverges from the current norm.
- **Be proactive** — Raise potential issues, inconsistencies, or improvement opportunities even when not explicitly asked. This includes security concerns, outdated dependencies, documentation gaps, and compatibility issues.
- **Draft PRs automatically** — After completing a code or documentation change, produce a pull request description draft without waiting to be asked.
- **Detailed explanations** — Provide thorough context for recommendations: why a pattern was chosen, what alternatives were considered, and how the change relates to the broader project goals.

## Code and documentation standards

- Follow the AsciiDoc conventions established in `shared-doc/` and existing `README-source.adoc` files.
- Ignore README.adoc files, those are flat - no includes - versions of README-source.adoc, which are properly rendered on GitHub, and are automatically rebuilt when README-source.adoc changes are pushed/merged.
- Use attribute substitution (`{productName}`, `{javaVersion}`, etc.) consistently — never hardcode values that are defined as attributes.
- Maven changes should align on all Quickstarts, preferably using dependencies managed by WildFly BOMs.
- Integration tests should use patterns already established in the repo (Arquillian, etc.) and be structured for reuse across quickstarts where possible.
- OpenShift compatibility changes should be validated against the existing compatible quickstarts as reference implementations.

## Communication preferences

- **Conciseness in summaries, detail in explanations** — Short summary up front, full reasoning below. Don't pad responses but don't omit rationale either.
- **Structured output** — Use tables, lists, and headers to organise complex information (e.g. comparing options, listing affected quickstarts).
- **Flag scope** — Always note when a change affects only one quickstart vs. multiple vs. the whole repo.
- **Ownership awareness** — When a change touches a quickstart owned by someone other than the project lead, flag this and suggest coordination steps.

## Workflow preferences

- Check `CODEOWNERS` and the quickstart's `README-source.adoc` to identify the right owner before proposing changes that belong to a specific quickstart team.
- When modernising, compare against the latest WildFly and Jakarta EE/MicroProfile spec versions — don't assume the existing code is current.
- For documentation changes, verify that shared fragments in `shared-doc/` aren't a better place for the content than inline in a specific quickstart.
- Prefer batch improvements: if fixing a pattern in one quickstart, identify all other quickstarts with the same issue and address them together.

## GitHub Actions canonical versions

Always prefer the versions used in existent `.github/workflows/` files. Align any new or updated workflow to these before committing.