package com.escodro.designsystem.components.kuvio.icon

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * User icon component.
 *
 * @param modifier the modifier to be applied to the icon
 * @param tint the color to be applied to the icon
 */
@Composable
fun KuvioUserIcon(
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface,
) {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "User",
        tint = tint,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun KuvioUserIconLightPreview() {
    AlkaaThemePreview {
        KuvioUserIcon(modifier = Modifier.size(48.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioUserIconDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioUserIcon(modifier = Modifier.size(48.dp))
    }
}
