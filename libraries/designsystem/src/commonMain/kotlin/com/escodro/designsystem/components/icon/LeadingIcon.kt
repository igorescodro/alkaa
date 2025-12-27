package com.escodro.designsystem.components.icon

import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

private const val TrailingLeadingAlpha = 0.50f

/**
 * Composable function to display a leading icon with customizable content description and modifier.
 *
 * @param imageVector the vector image to be displayed as the leading icon
 * @param contentDescription the description of the icon for accessibility purposes
 * @param modifier the Modifier to be applied to this icon (default is Modifier)
 */
@Composable
fun LeadingIcon(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.primary.copy(alpha = TrailingLeadingAlpha),
        modifier = modifier,
    )
}

@Preview
@Composable
private fun LeadingIconPreview() {
    AlkaaThemePreview {
        LeadingIcon(
            imageVector = androidx.compose.material.icons.Icons.Default.Alarm,
            contentDescription = "Leading Icon Preview",
        )
    }
}
