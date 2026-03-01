package com.escodro.designsystem.components.v2.item

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.v2.badge.BadgeCounter
import com.escodro.designsystem.components.v2.icon.EmojiIcon
import com.escodro.designsystem.components.v2.text.BodySmallText
import com.escodro.designsystem.components.v2.text.TitleMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * A single row in the Home / Lists screen that displays an icon on the start,
 * followed by a column with the title and subtitle, and an optional badge on the end.
 *
 * The component is rendered as a [Surface] with a medium shape. It includes:
 * - A background color that adapts based on selection ([TaskListItemData.isSelected]).
 * - A [Row] with horizontal padding of 12.dp and vertical padding of 11.dp.
 * - An [EmojiIcon] at the start with a size of 38.dp.
 * - A [Column] containing [TitleMediumText] and [BodySmallText] for title and subtitle.
 * - An optional [BadgeCounter] at the end, displayed only if [TaskListItemData.pendingCount] > 0.
 *
 * The row adapts its background for selected/unselected states and hides the
 * badge automatically when [TaskListItemData.pendingCount] is zero.
 *
 * @param data the display data for the item
 * @param onClick callback to be invoked when the item is clicked
 * @param modifier the modifier to be applied to the layout
 */
@Composable
fun TaskListItem(
    data: TaskListItemData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = if (data.isSelected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        color = backgroundColor,
        tonalElevation = if (data.isSelected) 0.dp else 1.dp,
        shadowElevation = if (data.isSelected) 0.dp else 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EmojiIcon(
                emoji = data.emoji,
                tint = data.iconTint,
                modifier = Modifier.size(38.dp),
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                TitleMediumText(
                    text = data.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                BodySmallText(
                    text = data.subtitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (data.pendingCount > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                BadgeCounter(
                    count = data.pendingCount,
                    isSelected = data.isSelected,
                )
            }
        }
    }
}

/**
 * Holds all display data for a single task-list row in the Home screen.
 *
 * @property emoji         Emoji character rendered inside the colored icon tile.
 * @property iconTint      Background tint of the icon tile (translucent accent color).
 * @property name          Primary list name, e.g. "Inbox".
 * @property subtitle      Supporting line, e.g. "3 tasks due today".
 * @property pendingCount  Number of pending tasks shown in the trailing badge.
 *                         Pass `0` to hide the badge entirely.
 * @property isSelected    When `true` the row receives the active highlight treatment.
 */
@Immutable
data class TaskListItemData(
    val emoji: String,
    val iconTint: Color,
    val name: String,
    val subtitle: String,
    val pendingCount: Int = 0,
    val isSelected: Boolean = false,
)

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemSelectedLightPreview() {
    AlkaaThemePreview {
        TaskListItem(
            data = TaskListItemData(
                emoji = "📋",
                iconTint = Color(0xFF1A6FD4).copy(alpha = 0.10f),
                name = "Inbox",
                subtitle = "3 tasks due today",
                pendingCount = 8,
                isSelected = true,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemUnselectedLightPreview() {
    AlkaaThemePreview {
        TaskListItem(
            data = TaskListItemData(
                emoji = "🚀",
                iconTint = Color(0xFFE07030).copy(alpha = 0.10f),
                name = "Work",
                subtitle = "Sprint tasks",
                pendingCount = 14,
                isSelected = false,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemNoBadgeLightPreview() {
    AlkaaThemePreview {
        TaskListItem(
            data = TaskListItemData(
                emoji = "✈️",
                iconTint = Color(0xFF1099B0).copy(alpha = 0.10f),
                name = "Travel",
                subtitle = "All caught up!",
                pendingCount = 0,
                isSelected = false,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun TaskListItemSelectedDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        TaskListItem(
            data = TaskListItemData(
                emoji = "📋",
                iconTint = Color(0xFF2B8CF4).copy(alpha = 0.15f),
                name = "Inbox",
                subtitle = "3 tasks due today",
                pendingCount = 8,
                isSelected = true,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun TaskListItemUnselectedDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        TaskListItem(
            data = TaskListItemData(
                emoji = "🌿",
                iconTint = Color(0xFF34C98C).copy(alpha = 0.15f),
                name = "Personal",
                subtitle = "Life & wellness",
                pendingCount = 6,
                isSelected = false,
            ),
            onClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
