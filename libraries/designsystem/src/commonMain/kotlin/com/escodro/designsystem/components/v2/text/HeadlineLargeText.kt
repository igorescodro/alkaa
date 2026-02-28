package com.escodro.designsystem.components.v2.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Headline large text component.
 *
 * @param text the text to be shown
 * @param modifier the modifier to be applied to the text
 * @param color the color to be applied to the text
 */
@Composable
fun HeadlineLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Preview
@Composable
private fun HeadlineLargeTextPreview() {
    AlkaaThemePreview {
        HeadlineLargeText(text = "Headline Large")
    }
}
