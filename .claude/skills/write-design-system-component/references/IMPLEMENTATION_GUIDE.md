# Implementation Guide: Kuvio Component

## Step 1: Extract Design Spec

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

---

## Step 2: Study Existing Components

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

---

## Step 3: Choose Output Directory

Place under: `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/<folder>/`

| Category | Folder | Examples |
|----------|--------|----------|
| Text variants | `text/` | `KuvioBodyMediumText`, `KuvioLabelSmallText` |
| Icons/Avatars | `icon/` | `KuvioEmojiIcon`, `KuvioAvatar` |
| Data display | `card/` | `KuvioCounterCard`, `KuvioTaskCard` |
| Badges/Tags | `badge/` | `KuvioBadge`, `KuvioChip` |
| List items | `item/` | `KuvioTaskItem`, `KuvioListItem` |
| Modals/Overlays | `dialog/` | `KuvioDialog`, `KuvioAlertDialog` |
| New category | `<lowercase-no-spaces>/` | Create if no match |

---

## Step 4: Implement Component

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

---

## Step 6: Add Previews

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

---

## Step 7: Verify Code Quality

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

---

## Step 8: Report

List files created/modified and describe:
- Variants exposed
- Slots available
- Dependencies on other Kuvio components
- Design tokens used
