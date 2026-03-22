---
name: write-design-system-component
description: Use when implementing a new Kuvio component for the Alkaa Design System after design spec and structure decisions are finalized
---

# Kuvio Component Implementation

## Overview

Kuvio is the Alkaa Design System's component library. This skill guides implementation of theme-aware, multiplatform Compose Multiplatform components following strict naming, structure, and styling conventions.

**Prerequisite:** Design spec must be finalized (variants, layout, interactive states) before using this skill. If not, use `superpowers:brainstorming` first.

## Steps

1. **Extract Design Spec** — Identify visual structure, variants, spacing, typography, interactive states → see `references/IMPLEMENTATION_GUIDE.md`
2. **Study Existing Components** — Read at least 2 reference implementations → see `references/IMPLEMENTATION_GUIDE.md`
3. **Choose Output Directory** — Map component category to folder → see `references/IMPLEMENTATION_GUIDE.md`
4. **Implement Component** — Follow naming, code style, and slot conventions → see `references/IMPLEMENTATION_GUIDE.md`
5. **Externalize Strings** — Use `localization` skill for all user-visible strings
6. **Add Previews** — Light + dark pair for every meaningful variant → see `references/IMPLEMENTATION_GUIDE.md`
7. **Verify Code Quality** — Run `scripts/verify_quality.sh`
8. **Report** — List files created, variants exposed, slots, dependencies, design tokens used → see `references/IMPLEMENTATION_GUIDE.md`

## Quick Reference

| Rule | Correct | Wrong |
|------|---------|-------|
| **Naming** | `KuvioTaskChip` | `TaskChip`, `TASK_CHIP` |
| **Strings** | `stringResource(Res.string.kuvio_component_label)` | `"Add task"` |
| **Colors** | `MaterialTheme.colorScheme.primary` | `Color(0xFF123456)` |
| **Text** | `KuvioBodyMediumText()` | `Text(fontSize=14.sp, ...)` |
| **Size** | ~60 lines, max 3 nesting levels | 200+ lines, deeply nested |
| **Slots** | `icon: (@Composable () -> Unit)?` | `iconColor: Color, iconSize: Dp, ...` |
| **Constants** | `private const val AddTaskPlaceholder` | `private const val ADD_TASK_PLACEHOLDER` |

## Red Flags — STOP and Review

**Before Implementation:**
- [ ] No design spec or spec is incomplete → STOP. Use `superpowers:brainstorming` first.
- [ ] Unsure which variants, slots, or states the component needs → STOP. Finalize design first.

**During Implementation:**
- [ ] About to hardcode a color → Use `MaterialTheme.colorScheme.*`
- [ ] Function approaching 80+ lines → Extract private sub-composables
- [ ] Nesting deeper than 3 levels → Extract logical chunks
- [ ] Raw `Text()` instead of Kuvio text primitive → Use `KuvioBodyMediumText()` etc.
- [ ] Strings in code (except Preview constants) → Move to `resources/strings.xml`

**After Writing Code:**
- [ ] Skipping quality check → Run `scripts/verify_quality.sh`
- [ ] Preview doesn't match design spec → Fix before finishing

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Hardcoding colors | Breaks dark mode — use `MaterialTheme.colorScheme.*` |
| Raw `Text()` instead of Kuvio text | Inconsistent typography — use `KuvioBodyMediumText()` etc. |
| Strings in code | Not localizable — move to `resources/strings.xml` |
| Function > 60 lines | Hard to read — extract private sub-composables |
| Deep nesting (4+ levels) | Extract logical chunks to private composables |
| Configuration objects | Hard to extend — use composable `(@Composable () -> Unit)?` slots |
| Skipping previews | Can't verify visually — add light/dark pair for each variant |
| `Canvas` for shapes | Hard to theme — use `Box`, `clip`, `background` modifiers |

## Design System Context

- `libraries/designsystem/` — Kuvio components, theme, tokens
- `resources/` — Strings, colors, spacing
- Reference: `/assets/designsystem/alkaa-ds.html`
