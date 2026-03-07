package com.escodro.designsystem.components.kuvio.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Clock icon component.
 *
 * @param modifier the modifier to be applied to the icon
 * @param tint the color to be applied to the icon
 */
@Composable
fun KuvioClockIcon(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        imageVector = Icons.Default.Schedule,
        contentDescription = "Clock",
        tint = tint,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun KuvioClockIconLightPreview() {
    AlkaaThemePreview {
        KuvioClockIcon(modifier = Modifier.size(48.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioClockIconDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioClockIcon(modifier = Modifier.size(48.dp))
    }
}
