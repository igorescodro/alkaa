package com.escodro.designsystem.components.kuvio.text

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Headline large text component.
 *
 * @param text the text to be shown
 * @param modifier the modifier to be applied to the text
 * @param color the color to be applied to the text
 * @param overflow how visual overflow should be handled
 * @param softWrap whether the text should break at soft line breaks
 * @param maxLines an optional maximum number of lines for the text to span, wrapping if necessary
 * @param minLines the minimum height in terms of number of lines
 * @param textDecoration the text decoration to be applied to the text
 */
@Suppress("LongParameterList")
@Composable
fun KuvioHeadlineLargeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    textDecoration: TextDecoration? = null,
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = MaterialTheme.typography.headlineLarge,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        textDecoration = textDecoration,
    )
}

@Preview(showBackground = true)
@Composable
private fun KuvioHeadlineLargeTextLightPreview() {
    AlkaaThemePreview(isDarkTheme = false) {
        KuvioHeadlineLargeText(text = "Headline Large")
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioHeadlineLargeTextDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioHeadlineLargeText(text = "Headline Large")
    }
}
