package com.escodro.designsystem.components.v2.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Body medium text component.
 *
 * @param text the text to be shown
 * @param modifier the modifier to be applied to the text
 * @param color the color to be applied to the text
 * @param overflow how visual overflow should be handled
 * @param softWrap whether the text should break at soft line breaks
 * @param maxLines an optional maximum number of lines for the text to span, wrapping if necessary
 * @param minLines the minimum height in terms of number of lines
 */
@Suppress("LongParameterList")
@Composable
fun BodyMediumText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.bodyMedium,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
    )
}

@Preview(showBackground = true)
@Composable
private fun BodyMediumTextLightPreview() {
    AlkaaThemePreview(isDarkTheme = false) {
        BodyMediumText(text = "Body Medium")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun BodyMediumTextDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        BodyMediumText(text = "Body Medium")
    }
}
