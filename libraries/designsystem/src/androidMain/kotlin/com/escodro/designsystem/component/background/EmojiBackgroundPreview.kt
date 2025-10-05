package com.escodro.designsystem.component.background

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.background.EmojiBackground
import com.escodro.designsystem.preview.PreviewTheme

@Preview
@Composable
private fun EmojiBackgroundPreview(
    @PreviewParameter(EmojiBackgroundPreviewProvider::class) emojiBackground: EmojiBackgroundData,
) {
    PreviewTheme {
        EmojiBackground(
            emojis = emojiBackground.emojis,
            color = emojiBackground.color,
            modifier = emojiBackground.modifier,
        )
    }
}

private class EmojiBackgroundPreviewProvider : PreviewParameterProvider<EmojiBackgroundData> {

    override val values: Sequence<EmojiBackgroundData> = sequenceOf(
        EmojiBackgroundData(
            emojis = listOf("🎉", "🥳", "🎊", "🎈", "🎁", "🪅"),
            color = Color(0xFF2196F3),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
                .clip(CircleShape),
        ),
        EmojiBackgroundData(
            emojis = listOf("🍔", "🍕", "🍟", "🌮", "🍜", "🍖"),
            color = Color(0xFFE91E63),
            modifier = Modifier
                .height(100.dp)
                .width(128.dp)
                .clip(RoundedCornerShape(size = 16.dp)),
        ),
        EmojiBackgroundData(
            emojis = listOf("🎶", "📚", "🎧", "🎭", "🖥️", "🖼️"),
            color = Color(0xFF4CAF50),
            modifier = Modifier.height(300.dp),
        ),
        EmojiBackgroundData(
            emojis = listOf("🏀", "⚽️", "🏅", "⚾️", "🏓", "🎳"),
            color = Color(0xFFFF9800),
            modifier = Modifier
                .height(150.dp)
                .width(400.dp)
                .clip(AbsoluteCutCornerShape(topLeft = 16.dp, bottomRight = 16.dp)),
        ),
    )
}

private data class EmojiBackgroundData(
    val emojis: List<String>,
    val color: Color,
    val modifier: Modifier,
)
