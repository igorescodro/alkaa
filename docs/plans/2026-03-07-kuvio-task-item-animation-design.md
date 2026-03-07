# KuvioTaskItem Radio Button Animation — Design Document

**Date:** 2026-03-07
**Component:** KuvioTaskItem — Radio Button Animation
**Location:** `libraries/designsystem/src/commonMain/kotlin/com/escodro/designsystem/components/kuvio/item/KuvioTaskItem.kt`

---

## Overview

Add smooth 200ms EaseInOut animations to the `TaskRadioButton` when toggling between checked/unchecked states. Both background fill and checkmark fade-in occur in parallel for a polished visual effect.

---

## Animation Specifications

| Aspect | Spec |
|--------|------|
| **Type** | Combined (background + checkmark) |
| **Duration** | 200ms |
| **Easing** | EaseInOutQuad |
| **Trigger** | `state == KuvioTaskItemState.COMPLETED` |

---

## Visual Flow

### Unchecked → Checked

1. **Background color animation** (200ms)
   - From: Transparent with outline border
   - To: Filled with `categoryColor` (or `primary` fallback)
   - Uses: `animateColorAsState`

2. **Checkmark fade-in** (200ms, parallel)
   - From: Invisible (alpha 0)
   - To: White checkmark icon visible
   - Uses: `AnimatedVisibility` with `fadeIn()`

### Checked → Unchecked

1. **Checkmark fade-out** (200ms)
   - From: White checkmark visible
   - To: Invisible
   - Uses: `AnimatedVisibility` with `fadeOut()`

2. **Background color animation** (200ms, parallel)
   - From: Filled circle
   - To: Transparent with outline border
   - Uses: `animateColorAsState`

---

## Implementation Details

**File:** `TaskRadioButton` composable in `KuvioTaskItem.kt`

### Imports to add

```kotlin
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.animation.animateColorAsState
```

### Current structure (no animation)

```kotlin
Box(
    contentAlignment = Alignment.Center,
    modifier = modifier
        .size(28.dp)
        .clip(CircleShape)
        .then(
            if (isChecked) {
                Modifier.background(resolvedColor)
            } else {
                Modifier.border(...)
            }
        )
        .clickable(onClick = onClick),
) {
    if (isChecked) {
        KuvioCompleteIcon(...)
    }
}
```

### Changes needed

1. **Extract animated background color:**
   ```kotlin
   val animatedBackgroundColor = animateColorAsState(
       targetValue = if (isChecked) resolvedColor else Color.Transparent,
       animationSpec = tween(durationMillis = 200, easing = EaseInOutQuad),
   )
   ```

2. **Apply animated color to background modifier:**
   ```kotlin
   .background(animatedBackgroundColor.value)
   ```

3. **Wrap checkmark in AnimatedVisibility:**
   ```kotlin
   AnimatedVisibility(
       visible = isChecked,
       enter = fadeIn(animationSpec = tween(200, easing = EaseInOutQuad)),
       exit = fadeOut(animationSpec = tween(200, easing = EaseInOutQuad)),
   ) {
       KuvioCompleteIcon(...)
   }
   ```

4. **Remove the static `if (isChecked)` conditional** for the checkmark (replaced by AnimatedVisibility)

---

## Design System Consistency

- **Duration 200ms** matches project's `FadeInTransition` and `FadeOutTransition`
- **EaseInOutQuad** provides polished motion (smoother than project's default `LinearEasing`)
- **animateColorAsState** is standard Compose pattern for color transitions
- **AnimatedVisibility** is standard Compose pattern for content entry/exit

---

## Testing

Verify in Android Studio preview panel:

1. **Light theme previews:** Animations smooth on all 5 scenarios
2. **Dark theme previews:** Animations smooth on all 5 scenarios
3. **State transitions:** Both directions (Pending→Completed and Completed→Pending) animate smoothly
4. **No jank:** Animations are 60fps without stuttering

---

## Success Criteria

✅ Radio button smoothly animates between states
✅ Checkmark fades in/out while background fills/unfills
✅ All previews show smooth animations
✅ Code passes all quality checks (ktlint, detekt, lint)
✅ No breaking API changes
