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
 * A single row in the Home / Lists screen that displays:
 *
 * ┌──────────────────────────────────────────────────────────┐
 * │  [icon]   List name                              [badge] │
 * │           Subtitle                                       │
 * └──────────────────────────────────────────────────────────┘
 *
 * The row adapts its background for selected/unselected states and hides the
 * badge automatically when [TaskListItemData.pendingCount] is zero.
 */
@Composable
fun TaskListItem(
    data: TaskListItemData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = when {
        data.isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        else -> MaterialTheme.colorScheme.surface
    }

    val borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.medium),
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
                modifier = Modifier.size(38.dp)
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
 * @param emoji         Emoji character rendered inside the colored icon tile.
 * @param iconTint      Background tint of the icon tile (translucent accent color).
 * @param name          Primary list name, e.g. "Inbox".
 * @param subtitle      Supporting line, e.g. "3 tasks due today".
 * @param pendingCount  Number of pending tasks shown in the trailing badge.
 *                      Pass `0` to hide the badge entirely.
 * @param isSelected    When `true` the row receives the active highlight treatment.
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

@Preview(name = "Selected — Light", showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemSelectedLightPreview() {
    AlkaaThemePreview() {
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

@Preview(name = "Unselected — Light", showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemUnselectedLightPreview() {
    AlkaaThemePreview() {
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

@Preview(name = "No Badge — Light", showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun TaskListItemNoBadgeLightPreview() {
    AlkaaThemePreview() {
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

@Preview(name = "Selected — Dark", showBackground = true, backgroundColor = 0xFF0F1B2D)
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

@Preview(name = "Unselected — Dark", showBackground = true, backgroundColor = 0xFF0F1B2D)
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
