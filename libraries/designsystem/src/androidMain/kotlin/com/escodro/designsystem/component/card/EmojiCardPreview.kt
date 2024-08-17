package com.escodro.designsystem.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.card.EmojiCard
import com.escodro.designsystem.preview.PreviewTheme

@PreviewLightDark
@Composable
private fun EmojiCardPreview(
    @PreviewParameter(EmojiCardPreviewProvider::class) emojiCard: EmojiCardData,
) {
    PreviewTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(32.dp),
        ) {
            EmojiCard(
                title = emojiCard.title,
                emojis = emojiCard.emojis,
                color = emojiCard.color,
                modifier = emojiCard.modifier,
            )
        }
    }
}

private class EmojiCardPreviewProvider : PreviewParameterProvider<EmojiCardData> {

    override val values: Sequence<EmojiCardData> = sequenceOf(
        EmojiCardData(
            title = "Party",
            emojis = listOf("🎉", "🥳", "🎊", "🎈", "🎁", "🪅"),
            color = Color(0xFF2196F3),
            modifier = Modifier
                .height(200.dp)
                .width(200.dp),
        ),
        EmojiCardData(
            title = "Food",
            emojis = listOf("🍔", "🍕", "🍟", "🌮", "🍜", "🍖"),
            color = Color(0xFFE91E63),
            modifier = Modifier
                .height(100.dp)
                .width(128.dp),
        ),
        EmojiCardData(
            title = "Arts",
            emojis = listOf("🎶", "📚", "🎧", "🎭", "🖥️", "🖼️"),
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .height(300.dp)
                .width(500.dp),
        ),
        EmojiCardData(
            title = "Sports",
            emojis = listOf("🏀", "⚽️", "🏅", "⚾️", "🏓", "🎳"),
            color = Color(0xFFFF9800),
            modifier = Modifier
                .height(150.dp)
                .width(400.dp),
        ),
    )
}

private data class EmojiCardData(
    val title: String,
    val emojis: List<String>,
    val color: Color,
    val modifier: Modifier,
)
