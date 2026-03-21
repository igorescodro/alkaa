---
name: localization
description: Use when adding, editing, or reviewing user-facing strings in the Alkaa project — UI labels, button text, content descriptions, empty states, error messages, plurals, or any text visible to the user.
---

# Alkaa Strings

## Overview

All user-facing strings live in the `resources` module and must be translated for every supported language. Non-user-facing strings (previews, logs) may be hardcoded.

## Naming Convention

```
<feature-name>_<component-name>_<description>
```

| Example | Feature | Component | Description |
|---------|---------|-----------|-------------|
| `task_list_header_empty` | task | list | header_empty |
| `category_cd_add_category` | category | — | cd_add_category |
| `task_alarm_permission_dialog_title` | task | alarm_permission_dialog | title |

Use `_cd_` in the component/description segment for content descriptions.

## Where to Add

- **File:** `resources/src/commonMain/composeResources/values/strings.xml`
- **Plurals:** `resources/src/commonMain/composeResources/values/plurals.xml`

Place new strings:
- **Next to existing entries** for the same feature, OR
- **In a new `<!-- Feature -->` comment block** if no block exists yet

```xml
<!-- MyFeature -->
<string name="myfeature_screen_title">My Title</string>
<string name="myfeature_button_save">Save</string>
<string name="myfeature_cd_close">Close</string>
```

## Supported Languages

Provide a translation in **all four** language files:

| Directory | Language |
|-----------|----------|
| `values/` | English (default) |
| `values-es/` | Spanish |
| `values-fr/` | French |
| `values-pt-rBR/` | Portuguese (Brazil) |

Each language directory contains `strings.xml` and `plurals.xml`. Add the new entry to the matching location in each file.

## Plurals

Same rules apply. Use `plurals.xml` instead of `strings.xml`:

```xml
<!-- Tracker -->
<plurals name="tracker_message_title">
    <item quantity="one">%1$d completed task</item>
    <item quantity="other">%1$d completed tasks</item>
</plurals>
```

## What NOT to Localize

Hardcode strings that are **not user-facing**:
- Composable preview annotations (`name = "TaskItem - Loading"`)
- Log/debug messages
- Internal identifiers

## Common Mistakes

| Mistake | Fix |
|---------|-----|
| Reusing a string across two features | Create a separate entry per feature, even if text is identical |
| Adding only to `values/` | Add to all four language directories |
| Hardcoding a content description | Content descriptions are user-facing — add to `resources` |
| Skipping plurals translation | Apply the same four-language rule to `plurals.xml` |
