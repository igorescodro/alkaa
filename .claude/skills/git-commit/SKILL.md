---
name: git-commit
description: Use when the user asks to commit changes, stage and commit, or says "commit my changes" — stages all uncommitted changes and creates a structured commit message with emoji, title, and summary
---

# Git Commit

## Overview

Stages and commits all uncommitted changes with a structured message: a random emoji, a concise title, and a short summary. Optionally adds a `Co-Authored-By` line if the user mentions agent helped.

**Permissions required:** `git commit`, `git rebase`

## Commit Message Format

```
[emoji] [title]

[summary — 1 to 5 lines depending on complexity]
```

With co-authorship:

```
[emoji] [title]

[summary — 1 to 5 lines depending on complexity]

Co-Authored-By: [model-name] [model-version] <model-email>
```

**Rules:**
- Title line (emoji + text): max 100 characters
- Summary lines: max 100 characters each
- Summary length: 1 line for trivial changes, up to 5 lines for complex ones
- Emoji: pick a random emoji, do not map specific emojis to change types
- Co-authorship: add only when the user explicitly says Claude helped or co-authored

## Steps

1. Run `git diff` and `git status` to understand all uncommitted changes
2. Analyze the changes to determine:
   - A concise, descriptive title
   - An appropriate emoji
   - A summary proportional to complexity
3. Stage changes: `git add <specific files>` — avoid `git add -A` unless all changes should be committed
4. Commit using a HEREDOC to preserve formatting:

```bash
git commit -m "$(cat <<'EOF'
🔧 Title of the change

Summary of the change, respecting the 100-char line limit.
Add more lines only if the change is complex enough to warrant it.
EOF
)"
```

With co-authorship:

```bash
git commit -m "$(cat <<'EOF'
✨ Title of the change

Summary of the change.

Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>
EOF
)"
```

5. Run `git status` to confirm the commit succeeded

## Co-Authorship

Use the model name and version from the current environment. The model ID `claude-sonnet-4-6` becomes `Claude Sonnet 4.6`. Format:

```
Co-Authored-By: Claude [Model Name] [Version] <noreply@anthropic.com>
```

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Title over 100 chars | Shorten — cut adjectives, use imperative verbs |
| Summary too long for a trivial change | 1 line is enough for small fixes |
| Adding co-authorship unprompted | Only add when user explicitly requests it |
| Using `git add -A` blindly | Stage specific files to avoid committing secrets or unrelated work |
| Skipping `git status` after commit | Always verify the commit landed correctly |
