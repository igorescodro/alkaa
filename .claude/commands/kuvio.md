---
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
argument-hint: <ComponentName>
---

Implement a new Kuvio component for the Alkaa Design System.

The component to implement is: **$ARGUMENTS**

## Steps

### 1. Read the design spec

Read the design system HTML to find the component section:

```bash
grep -n "section-title\|subsection-title\|id=" /Users/igorescodro/StudioProjects/alkaa/assets/designsystem/alkaa-ds.html | grep -i "$ARGUMENTS"
```

Then read the relevant HTML section (use line numbers from the grep output):

```bash
sed -n '<start>,<end>p' /Users/igorescodro/StudioProjects/alkaa/assets/designsystem/alkaa-ds.html
```

Extract from the HTML:

- Visual structure and layout
- Variants (sizes, states, color roles)
- Spacing/sizing tokens
- Typography used
- Interactive states

### 2. Study existing Kuvio components

Read at least 2 existing components as code reference:

-
`libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/card/KuvioCounterCard.kt`
-
`libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/dialog/KuvioDialog.kt`
-
`libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/icon/KuvioEmojiIcon.kt`
-
`libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/text/KuvioBodyMediumText.kt`

Also check the badge and item components for more patterns.

### 3. Choose the output directory

Place the file(s) under:

```
libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/<folder>/
```

Use an existing folder if the component fits a category already present (`text`, `icon`, `card`,
`badge`, `item`, `dialog`). Otherwise create a new folder named after the component category (
lowercase, no spaces).

### 4. Implement the component

Follow these rules strictly:

- **Naming**: All public composables must start with `Kuvio` prefix (e.g. `KuvioTaskChip`,
  `KuvioAvatar`)
- **Package**: `com.escodro.designsystem.components.kuvio.<folder>`
- **Reuse**: Always use existing Kuvio text components (`KuvioBodyMediumText`,
  `KuvioLabelMediumText`, etc.) instead of raw `Text`. Reuse `KuvioEmojiIcon`,
  `KuvioDialogIconContainer`, etc. where applicable
- **No canvas**: Do not use `Canvas` to draw shapes, icons, or backgrounds. Use `Box`, `Surface`,
  `Icon`, `clip`, `background` modifiers instead
- **Theme-aware**: Use `MaterialTheme.colorScheme.*` and `MaterialTheme.shapes.*` tokens — never
  hardcode colors except in Previews
- **Slots over config**: Prefer composable slot parameters (`icon: (@Composable () -> Unit)?`) over
  deeply nested configuration objects
- **Split files**: If the component has 3+ distinct sub-composables or is clearly multi-part, split
  into separate files in the same folder
- **KDoc**: Add KDoc for every public composable and its parameters
- **Extract sub-composables**: Keep each `@Composable` function under ~60 lines with no more than ~3
  levels of nesting. When a function grows beyond this, extract logical chunks (text fields, action
  rows, decoration boxes, icon groups, etc.) into private composables in the same file.
- **Constant naming**: Name all `private const val` string constants (content descriptions,
  placeholders, preview text, etc.) in **PascalCase**, not SCREAMING_SNAKE_CASE — e.g.
  `AddTaskPlaceholder`, not `ADD_TASK_PLACEHOLDER`.

### 5. Add strings to the resources module

For any user-visible strings in the component (labels, content descriptions, placeholders, etc.), *
*do not hardcode them in the composable**. Add them to the `resources/` module instead, in the
appropriate `strings.xml` files for each locale.

Name keys using the pattern `kuvio_<component>_<purpose>`, e.g. `kuvio_add_task_bar_placeholder` or
`kuvio_add_task_bar_cd_submit`.

**Strings for Previews may be hardcoded** as private `const val` constants at the bottom of the
file — they are never shipped to users.

### 6. Add Previews

Every file must have **both** a light and dark preview at the bottom:

```kotlin
@Preview(showBackground = true)
@Composable
private fun KuvioXxxLightPreview() {
    AlkaaThemePreview {
        // component with realistic sample data
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioXxxDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        // same component
    }
}
```

Use `AlkaaThemePreview` (imported from `com.escodro.designsystem.theme.AlkaaThemePreview`).

For components with multiple variants (sizes, states, colors), add one preview pair per meaningful
variant.

Place preview string constants as private `const val` at the bottom of the file.

### 7. Verify

After writing the files, first confirm compilation succeeds:

```bash
./gradlew :libraries:designsystem:compileKotlinDesktop 2>&1 | tail -30
```

Then run static analysis to catch naming, complexity, and style violations:

```bash
./gradlew :libraries:designsystem:detektCommonMainSourceSet ktlintCheck 2>&1 | grep -E "\.kt:|BUILD|FAILED"
```

Fix all reported issues before finishing. Common things to check:

- Constant names are PascalCase (`AddTaskPlaceholder`, not `ADD_TASK_PLACEHOLDER`)
- No composable exceeds ~60 lines — extract private sub-composables if needed
- Lambda parameter names are present-tense (`onFocus`, not `onFocused`)

### 8. Report

List the files created/modified and briefly describe the variants/slots exposed.
