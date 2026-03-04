---
allowed-tools: Read, Glob, Grep, Bash, Write
---

You are performing a production-readiness code review on the current branch compared to `main`.

## Steps

1. **Get the branch name and diff:**
   ```bash
   git rev-parse --abbrev-ref HEAD
   git diff HEAD origin/main --name-only
   git diff HEAD origin/main
   ```

2. **Read all changed files** in full to understand the complete context around the changes.

3. **Review the changes** focusing ONLY on substantive issues:
    - Bugs, crashes, null safety issues
    - Memory leaks (unclosed resources, missing lifecycle cleanup, leaked coroutines)
    - Security vulnerabilities (injection, exposed secrets, insecure storage)
    - Race conditions, thread safety issues
    - Architectural violations (wrong dependency direction, breaking module boundaries)
    - Android/Kotlin/KMP best practices violations that could cause real problems
    - Missing error handling that could cause crashes
    - Data loss risks

4. **Do NOT flag:**
    - Naming preferences or style opinions
    - Formatting or whitespace
    - Missing comments or documentation
    - "Could be more idiomatic" suggestions
    - Nitpicks or personal preferences
    - Things already handled by ktlint or detekt

5. **If there are no substantive issues, say so.** A clean review is a valid outcome.

6. **Save the review** to `.claude/reviews/review-$DATE.md` where `$DATE` is today's date (
   YYYY-MM-DD). If the file already exists, append a numeric suffix (e.g.,
   `review-2026-03-03-2.md`).

## Review Output Format

Use this exact format for the review file:

```markdown
# Code Review: <branch-name>

**Date:** <YYYY-MM-DD>
**Files changed:** <count>

## Summary

<2-3 sentence summary of what the changes do>

## Findings

### 1. 🔴 <Short title>

**File:** `<path>:<line>`

<Clear description of the issue and why it matters>

```kotlin
// Suggested fix (if applicable)
```

### 2. 🟠 <Short title>

**File:** `<path>:<line>`

<Description>

---

## Verdict

<One-line overall assessment: "Ready to merge", "Needs fixes before merge", etc.>

```

### Severity Levels

- 🔴 **Critical** — Bugs, crashes, security vulnerabilities, data loss risks
- 🟠 **Warning** — Memory leaks, performance issues, architectural concerns
- 🟡 **Info** — Best practice violations worth noting (use sparingly)

After saving, print the path to the review file.
