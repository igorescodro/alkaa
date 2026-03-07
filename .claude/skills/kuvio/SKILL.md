---
name: kuvio
description: Use when implementing a new Kuvio component for the Alkaa Design System after design spec and structure decisions are finalized
---

# Kuvio Component Implementation

## Overview

Kuvio is the Alkaa Design System's component library. Implementing a Kuvio component means translating a design spec into a reusable, theme-aware Compose Multiplatform composable following strict naming, structure, and styling conventions.

**Core principle:** Design specs and component architecture are finalized BEFORE implementation begins. This skill guides the implementation workflow.

## When to Use

**REQUIRED: Use AFTER these skills complete:**
- `superpowers:brainstorming` — Design decisions and component structure are finalized
- Design spec exists and is accessible (HTML design system reference or Figma)

**Use this skill when:**
- Design spec is defined (variants, layout, interactive states all decided)
- Component structure and slots are finalized
- Ready to write implementation code
- Component is new to the codebase

**Do NOT use when:**
- Still designing or debating structure (use `superpowers:brainstorming` first)
- Exploring whether component should exist (use `superpowers:writing-plans` first)
- Design decisions are unclear or incomplete
- Uncertain about variants, slots, or what to expose

**Optional but recommended:**
- `superpowers:test-driven-development` — Write preview tests first, implementation second
- Use TDD to verify component variants match design spec before finishing

## Before You Start: Verify Design Spec Is Ready

**Your design spec MUST include:**
- Visual structure (what goes where, layout)
- All variants (sizes, color roles, states, disabled/active)
- Spacing and sizing (how much padding, how far apart)
- Typography (which text style for each area)
- Interactive behavior (what happens on click, focus, etc.)

**If the spec is incomplete or unclear:**
- STOP. Do not proceed to implementation.
- Use `superpowers:brainstorming` to finalize the spec first.
- Unclear specs cause rework. Clarify BEFORE coding.

## Implementation Workflow

### Step 1: Extract Design Spec

Read the design system HTML to find the component section:

```bash
grep -n "section-title\|subsection-title\|id=" /path/to/alkaa-ds.html | grep -i "ComponentName"
```

Extract from spec:
- Visual structure and layout
- Variants (sizes, states, color roles)
- Spacing/sizing tokens
- Typography used
- Interactive states

### Step 2: Study Existing Components

Read at least 2 reference implementations:

- `KuvioCounterCard.kt` — complex card layout with slots
- `KuvioDialog.kt` — multi-part component with state management
- `KuvioEmojiIcon.kt` — simple icon component
- `KuvioBodyMediumText.kt` — text primitives
- Badge/item components for more patterns

Look for:
- How slots are exposed (`icon: (@Composable () -> Unit)?`)
- How tokens are used (`MaterialTheme.colorScheme.*`)
- How nesting is structured (max ~3 levels)
- How composables under ~60 lines are extracted

### Step 3: Choose Output Directory

Place under: `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/<folder>/`

| Category | Folder | Examples |
|----------|--------|----------|
| Text variants | `text/` | `KuvioBodyMediumText`, `KuvioLabelSmallText` |
| Icons/Avatars | `icon/` | `KuvioEmojiIcon`, `KuvioAvatar` |
| Data display | `card/` | `KuvioCounterCard`, `KuvioTaskCard` |
| Badges/Tags | `badge/` | `KuvioBadge`, `KuvioChip` |
| List items | `item/` | `KuvioTaskItem`, `KuvioListItem` |
| Modals/Overlays | `dialog/` | `KuvioDialog`, `KuvioAlertDialog` |
| New category | `<lowercase>-no-spaces/` | Create if no match |

### Step 4: Implement Component

**Naming Rules:**
- All public composables: `Kuvio<Name>` prefix (PascalCase)
- Package: `com.escodro.designsystem.components.kuvio.<folder>`
- Private sub-composables: lowercase with camelCase

**Code Style:**
- Reuse existing Kuvio primitives (`KuvioBodyMediumText`, `KuvioEmojiIcon`) — never raw `Text`
- No `Canvas` for shapes/icons — use `Box`, `Surface`, `Icon`, `clip`, `background`
- Theme-aware only: `MaterialTheme.colorScheme.*` and `MaterialTheme.shapes.*` — no hardcoded colors except Previews
- Slots over config: Prefer `icon: (@Composable () -> Unit)?` over nested objects
- Max ~60 lines per composable, max ~3 nesting levels
- Extract logical chunks into private composables when needed

**Structure:**
- File per composable (unless closely related)
- KDoc for every public function and parameter
- Constant naming: **PascalCase** (`AddTaskPlaceholder`, not `ADD_TASK_PLACEHOLDER`)

### Step 5: Externalize Strings

User-visible strings go in `resources/` module (NOT hardcoded):

```kotlin
// ❌ Wrong
Text("Add task here")

// ✅ Right - in resources/src/commonMain/resources/values/strings.xml
<string name="kuvio_add_task_bar_placeholder">Add task here</string>

// Then use via stringResource()
Text(stringResource(Res.string.kuvio_add_task_bar_placeholder))
```

String key pattern: `kuvio_<component>_<purpose>`

**Exception:** Preview constants may be hardcoded as `private const val` at file bottom.

### Step 6: Add Previews

Every file must have light AND dark previews:

```kotlin
@Preview(showBackground = true)
@Composable
private fun KuvioXxxLightPreview() {
    AlkaaThemePreview {
        // Component with realistic sample data
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioXxxDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        // Same component
    }
}
```

Use `AlkaaThemePreview` (from `com.escodro.designsystem.theme`).

For variants: One preview pair per meaningful variant (size, state, color).

Place preview strings as `private const val` at file bottom.

### Step 7: Verify Code Quality

Compilation check:
```bash
./gradlew :libraries:designsystem:compileKotlinDesktop 2>&1 | tail -30
```

Static analysis:
```bash
./gradlew :libraries:designsystem:detektCommonMainSourceSet ktlintCheck 2>&1 | grep -E "\.kt:|BUILD|FAILED"
```

Fix reported issues:
- Constant names are PascalCase (not SCREAMING_SNAKE_CASE)
- No composable exceeds ~60 lines
- Lambda names are present-tense (`onFocus`, not `onFocused`)
- All strings externalized to resources

### Step 8: Report

List files created/modified and describe:
- Variants exposed
- Slots available
- Dependencies on other Kuvio components
- Design tokens used

## Quick Reference: Key Rules

| Rule | Correct | Wrong |
|------|---------|-------|
| **Naming** | `KuvioTaskChip` | `TaskChip`, `TASK_CHIP` |
| **Strings** | `stringResource(Res.string.kuvio_component_label)` | `"Add task"` |
| **Colors** | `MaterialTheme.colorScheme.primary` | `Color(0xFF123456)` |
| **Text** | `KuvioBodyMediumText()` | `Text(fontSize=14.sp, ...)` |
| **Size** | ~60 lines, max 3 nesting | 200+ lines, deeply nested |
| **Slots** | `icon: (@Composable () -> Unit)?` | `iconColor: Color, iconSize: Dp, ...` |
| **Constants** | `private const val AddTaskPlaceholder = "Add task..."` | `private const val ADD_TASK_PLACEHOLDER` |

## Common Mistakes

| Mistake | Why Wrong | Fix |
|---------|-----------|-----|
| Hardcoding colors | Breaks dark mode, hard to theme | Use `MaterialTheme.colorScheme.*` |
| Raw `Text()` instead of Kuvio text | Inconsistent typography | Use `KuvioBodyMediumText()` etc. |
| Strings in code | Not localizable, violates pattern | Move to `resources/` strings.xml |
| Function > 60 lines | Hard to test, understand, reuse | Extract private sub-composables |
| Deep nesting (4+ levels) | Readability suffers | Extract logical chunks to private composables |
| Configuration objects | Hard to extend, test, compose | Use composable slots instead |
| Skipping previews | Can't verify visually | Add light/dark pair for each variant |
| `Canvas` for shapes | Unmaintainable, hard to theme | Use `Box`, `clip`, `background` modifiers |

## Red Flags - STOP and Review

If you notice any of these, you are about to violate the kuvio pattern:

**Before Implementation:**
- [ ] No design spec or spec is incomplete/unclear → STOP. Use brainstorming first.
- [ ] Unsure which variants, slots, or states the component needs → STOP. Finalize design first.
- [ ] Unclear which existing components to study → STOP. Ask which patterns apply to your component.

**During Implementation:**
- [ ] About to hardcode colors (even "just for this variant") → Use `MaterialTheme.colorScheme.*`
- [ ] Function approaching 80+ lines → Extract private sub-composables
- [ ] Nesting deeper than 3 levels → Extract logical chunks
- [ ] Raw `Text()` instead of Kuvio text primitive → Use `KuvioBodyMediumText()` etc.
- [ ] Strings in code (for Previews is fine) → Move to `resources/strings.xml`
- [ ] Tempted to skip previews due to time pressure → Write them. They catch bugs early.

**After Writing Code:**
- [ ] Skipping detekt/ktlint check → Run them. They catch style violations.
- [ ] Preview doesn't look right → Fix now, not after merge.
- [ ] Component doesn't match design spec in preview → Fix before finishing.

**All of these mean:** You are about to miss part of the kuvio pattern. Pause and correct before continuing.

## Design System Context

Key modules:
- `libraries/designsystem/` — Kuvio components, theme, tokens
- `resources/` — Strings (localization), colors, spacing
- Reference: `/assets/designsystem/alkaa-ds.html`

Kuvio components are multiplatform (Android/iOS/Desktop), theme-aware, accessible, and testable via previews.
