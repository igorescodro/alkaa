package com.escodro.designsystem.components.v2.badge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.v2.text.LabelMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Badge counter component.
 *
 * @param count the number to be shown in the badge
 * @param isSelected if the badge is selected
 * @param modifier the modifier to be applied to the badge
 */
@Composable
fun BadgeCounter(
    count: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    val badgeBackground = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val badgeContentColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(badgeBackground)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        LabelMediumText(
            text = count.toString(),
            color = badgeContentColor,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeCounterLightPreview() {
    AlkaaThemePreview {
        BadgeCounter(count = 3, isSelected = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun BadgeCounterSelectedLightPreview() {
    AlkaaThemePreview {
        BadgeCounter(count = 99, isSelected = true)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun BadgeCounterDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        BadgeCounter(count = 128, isSelected = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun BadgeCounterSelectedDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        BadgeCounter(count = 1, isSelected = true)
    }
}
