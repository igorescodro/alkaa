package com.escodro.designsystem.components.kuvio.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.icon.KuvioEmojiIcon
import com.escodro.designsystem.components.kuvio.icon.KuvioMoreIcon
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.components.kuvio.text.KuvioTitleLargeText
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.category_header_progress
import org.jetbrains.compose.resources.stringResource

/**
 * Header component for a Category Details screen.
 *
 * Displays the category emoji, name, task progress, and an options button.
 *
 * @param name the display name of the category.
 * @param color the color associated with the category, used as the emoji box background tint.
 * @param totalTasks total number of tasks in the category.
 * @param completedTasks number of completed tasks in the category.
 * @param onOptionsClick callback invoked when the options (more vert) button is tapped.
 * @param modifier modifier applied to the outermost [Row].
 * @param emoji optional emoji character to display inside the icon box. When null a fallback
 *   placeholder icon is shown in its place.
 */
@Composable
fun KuvioCategoryHeader(
    name: String,
    color: Color,
    totalTasks: Int,
    completedTasks: Int,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
    emoji: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        KuvioEmojiIcon(
            emoji = emoji ?: "\uD83D\uDCCB",
            tint = color.copy(alpha = 0.15f),
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            KuvioTitleLargeText(text = name)
            KuvioBodyMediumText(
                text = stringResource(Res.string.category_header_progress, totalTasks, completedTasks),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onOptionsClick) {
            KuvioMoreIcon(
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioCategoryHeaderLightPreview() {
    AlkaaThemePreview {
        KuvioCategoryHeader(
            name = "Work",
            color = Color(0xFF6200EA),
            totalTasks = 14,
            completedTasks = 3,
            onOptionsClick = {},
            emoji = "\uD83D\uDCBC",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioCategoryHeaderDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioCategoryHeader(
            name = "Work",
            color = Color(0xFF6200EA),
            totalTasks = 14,
            completedTasks = 3,
            onOptionsClick = {},
            emoji = "\uD83D\uDCBC",
        )
    }
}
