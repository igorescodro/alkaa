package com.escodro.designsystem.components.kuvio.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.components.kuvio.text.KuvioHeadlineSmallText
import com.escodro.designsystem.components.kuvio.text.KuvioTitleLargeText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Emoji icon component.
 *
 * @param emoji the emoji to be shown
 * @param tint the background tint to be applied to the icon
 * @param modifier the modifier to be applied to the icon
 */
@Composable
fun KuvioEmojiIcon(
    emoji: String,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(tint),
        contentAlignment = Alignment.Center,
    ) {
        BoxWithConstraints {
            when {
                maxWidth < 32.dp -> KuvioBodyMediumText(text = emoji)
                maxWidth < 48.dp -> KuvioTitleLargeText(text = emoji)
                else -> KuvioHeadlineSmallText(text = emoji)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioEmojiIconLightPreview() {
    AlkaaThemePreview {
        KuvioEmojiIcon(
            emoji = "🚀",
            tint = Color.Black,
            modifier = Modifier.size(48.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioEmojiIconMediumPreview() {
    AlkaaThemePreview {
        KuvioEmojiIcon(
            emoji = "🚀",
            tint = Color.Black,
            modifier = Modifier.size(40.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioEmojiIconSmallPreview() {
    AlkaaThemePreview {
        KuvioEmojiIcon(
            emoji = "🚀",
            tint = Color.Black,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioEmojiIconDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioEmojiIcon(
            emoji = "🌿",
            tint = Color.Green,
            modifier = Modifier.size(38.dp),
        )
    }
}
