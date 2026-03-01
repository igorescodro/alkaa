package com.escodro.designsystem.components.v2.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.v2.text.TitleLargeText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Emoji icon component.
 *
 * @param emoji the emoji to be shown
 * @param tint the background tint to be applied to the icon
 * @param modifier the modifier to be applied to the icon
 */
@Composable
fun EmojiIcon(
    emoji: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(tint),
        contentAlignment = Alignment.Center,
    ) {
        TitleLargeText(
            text = emoji,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmojiIconLightPreview() {
    AlkaaThemePreview {
        EmojiIcon(
            emoji = "🚀",
            tint = Color.Black,
            modifier = Modifier.size(38.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun EmojiIconDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        EmojiIcon(
            emoji = "🌿",
            tint = Color.Green,
            modifier = Modifier.size(38.dp)
        )
    }
}
