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

- `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/card/KuvioCounterCard.kt`
- `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/dialog/KuvioDialog.kt`
- `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/icon/KuvioEmojiIcon.kt`
- `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/text/KuvioBodyMediumText.kt`

Also check the badge and item components for more patterns.

### 3. Choose the output directory

Place the file(s) under:
```
libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/<folder>/
```

Use an existing folder if the component fits a category already present (`text`, `icon`, `card`, `badge`, `item`, `dialog`). Otherwise create a new folder named after the component category (lowercase, no spaces).

### 4. Implement the component

Follow these rules strictly:

- **Naming**: All public composables must start with `Kuvio` prefix (e.g. `KuvioTaskChip`, `KuvioAvatar`)
- **Package**: `com.escodro.designsystem.components.kuvio.<folder>`
- **Reuse**: Always use existing Kuvio text components (`KuvioBodyMediumText`, `KuvioLabelMediumText`, etc.) instead of raw `Text`. Reuse `KuvioEmojiIcon`, `KuvioDialogIconContainer`, etc. where applicable
- **No canvas**: Do not use `Canvas` to draw shapes, icons, or backgrounds. Use `Box`, `Surface`, `Icon`, `clip`, `background` modifiers instead
- **Theme-aware**: Use `MaterialTheme.colorScheme.*` and `MaterialTheme.shapes.*` tokens — never hardcode colors except in Previews
- **Slots over config**: Prefer composable slot parameters (`icon: (@Composable () -> Unit)?`) over deeply nested configuration objects
- **Split files**: If the component has 3+ distinct sub-composables or is clearly multi-part, split into separate files in the same folder
- **KDoc**: Add KDoc for every public composable and its parameters

### 5. Add Previews

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

For components with multiple variants (sizes, states, colors), add one preview pair per meaningful variant.

Place preview string constants as private `const val` at the bottom of the file.

### 6. Verify

After writing the files, run:
```bash
./gradlew :libraries:designsystem:compileKotlinDesktop 2>&1 | tail -30
```

Fix any compilation errors before finishing.

### 7. Report

List the files created/modified and briefly describe the variants/slots exposed.
