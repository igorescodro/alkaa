package com.escodro.designsystem.components.v2.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.v2.icon.EmojiIcon
import com.escodro.designsystem.components.v2.text.HeadlineLargeText
import com.escodro.designsystem.components.v2.text.LabelMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Counter card component to be used in the home screen.
 *
 * @param emoji the emoji to be shown
 * @param count the count to be shown
 * @param label the label to be shown
 * @param tint the background tint to be applied to the icon
 * @param modifier the modifier to be applied to the card
 * @param onClick the callback to be called when the card is clicked
 */
@Suppress("LongParameterList")
@Composable
fun CounterCard(
    emoji: String,
    count: Int,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            EmojiIcon(
                emoji = emoji,
                tint = tint,
                modifier = Modifier.size(28.dp),
            )
            HeadlineLargeText(
                text = count.toString(),
            )
            LabelMediumText(
                text = label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CounterCardLightPreview() {
    AlkaaThemePreview {
        CounterCard(
            emoji = "📅",
            count = 5,
            label = "Today",
            tint = Color(0xFFFFE0B2),
            modifier = Modifier.size(128.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun CounterCardDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        CounterCard(
            emoji = "🗓",
            count = 12,
            label = "Scheduled",
            tint = Color(0xFFC8E6C9),
            modifier = Modifier.height(128.dp).width(256.dp),
        )
    }
}
