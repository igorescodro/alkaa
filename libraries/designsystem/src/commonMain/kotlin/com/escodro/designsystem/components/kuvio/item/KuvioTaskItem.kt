package com.escodro.designsystem.components.kuvio.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.chip.KuvioTaskChip
import com.escodro.designsystem.components.kuvio.chip.KuvioTaskChipType
import com.escodro.designsystem.components.kuvio.icon.KuvioCompleteIcon
import com.escodro.designsystem.components.kuvio.text.KuvioTitleMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.kuvio_task_item_check_cd
import com.escodro.resources.kuvio_task_item_uncheck_cd
import org.jetbrains.compose.resources.stringResource

enum class KuvioTaskItemState { PENDING, COMPLETED, OVERDUE }

/**
 * Holds all display data for a single task item row.
 *
 * @property title         The task title text
 * @property chips         List of metadata chips (date, repeat, list, tags)
 * @property state         The completion state of the task
 * @property categoryColor Optional background tint color for the list category indicator
 */
@Immutable
data class KuvioTaskItemData(
    val title: String,
    val chips: List<KuvioTaskChipType> = emptyList(),
    val state: KuvioTaskItemState = KuvioTaskItemState.PENDING,
    val categoryColor: Color? = null,
)

private data class TaskItemVisuals(
    val cardBackground: Color,
    val titleColor: Color,
    val titleDecoration: TextDecoration,
)

@Composable
private fun resolveCardVisuals(state: KuvioTaskItemState): TaskItemVisuals = when (state) {
    KuvioTaskItemState.PENDING -> TaskItemVisuals(
        cardBackground = MaterialTheme.colorScheme.surface,
        titleColor = MaterialTheme.colorScheme.onSurface,
        titleDecoration = TextDecoration.None,
    )

    KuvioTaskItemState.COMPLETED -> TaskItemVisuals(
        cardBackground = MaterialTheme.colorScheme.surface,
        titleColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        titleDecoration = TextDecoration.LineThrough,
    )

    KuvioTaskItemState.OVERDUE -> TaskItemVisuals(
        cardBackground = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
        titleColor = MaterialTheme.colorScheme.onSurface,
        titleDecoration = TextDecoration.None,
    )
}

/**
 * Custom radio button styled as a circle.
 *
 * Renders as an outline circle when unchecked, and a filled circle with a checkmark when checked.
 * Uses the category color for the filled state, or falls back to primary color.
 *
 * @param state the current task state (determines if checked)
 * @param categoryColor optional color for the filled circle; falls back to primary color if null
 * @param contentDescription accessibility description that switches based on checked state
 * @param onClick callback to invoke when the radio button is clicked
 * @param modifier the modifier to be applied to the radio button
 */
@Composable
private fun TaskRadioButton(
    state: KuvioTaskItemState,
    categoryColor: Color?,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val resolvedColor = categoryColor ?: MaterialTheme.colorScheme.primary
    val isChecked = state == KuvioTaskItemState.COMPLETED
    val animatedBackgroundColor = animateColorAsState(
        targetValue = if (isChecked) resolvedColor else Color.Transparent,
        animationSpec = tween(durationMillis = 200, easing = EaseInOutQuad),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(animatedBackgroundColor.value, CircleShape)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
    ) {
        if (isChecked) {
            KuvioCompleteIcon(
                tint = Color.White,
                modifier = Modifier.size(14.dp),
                contentDescription = contentDescription,
            )
        }
    }
}

/**
 * A single task item row displaying title, metadata chips, completion state, and category indicator.
 *
 * The component renders with:
 * - A radio button or checkbox at the start for completion state toggling
 * - A title text in the center
 * - Metadata chips (date, repeat, list, tag) below the title
 * - A category color indicator on the start edge
 *
 * @param data the display data for the item
 * @param onItemClick callback to be invoked when the item row is clicked
 * @param onCheckClick callback to be invoked when the checkbox is clicked
 * @param modifier the modifier to be applied to the layout
 */
@Composable
fun KuvioTaskItem(
    data: KuvioTaskItemData,
    onItemClick: () -> Unit,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val visuals = resolveCardVisuals(data.state)
    val checkCd = if (data.state == KuvioTaskItemState.COMPLETED) {
        stringResource(Res.string.kuvio_task_item_uncheck_cd)
    } else {
        stringResource(Res.string.kuvio_task_item_check_cd)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = visuals.cardBackground,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TaskRadioButton(
                state = data.state,
                categoryColor = data.categoryColor,
                contentDescription = checkCd,
                onClick = onCheckClick,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                KuvioTitleMediumText(
                    text = data.title,
                    color = visuals.titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = visuals.titleDecoration,
                )

                if (data.chips.isNotEmpty()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        data.chips.forEach { chip ->
                            KuvioTaskChip(type = chip)
                        }
                    }
                }
            }
        }
    }
}

// ── Preview constants ─────────────────────────────────────────────────────────

private const val PreviewTitlePresentation = "Prepare Q3 presentation slides"

private const val PreviewTitleDesign = "Review design system proposals"

private const val PreviewTitleInvoice = "Send invoice to client"

private const val PreviewTitleTax = "Submit tax documents"

private const val PreviewTitleMeditate = "Meditate for 20 minutes"

private const val PreviewDateToday = "Today, 3 PM"

private const val PreviewDateSoon = "Thu, 10 AM"

private const val PreviewDateYesterday = "Yesterday"

private const val PreviewListWork = "Work"

private const val PreviewListIb = "IB"

private const val PreviewTagWellness = "#wellness"

private const val PreviewRepeatDaily = "Daily"

// ── Light previews ─────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioTaskItemPendingLightPreview() {
    AlkaaThemePreview {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitlePresentation,
                chips = listOf(
                    KuvioTaskChipType.DateToday(PreviewDateToday),
                    KuvioTaskChipType.List(PreviewListWork),
                ),
                state = KuvioTaskItemState.PENDING,
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioTaskItemUpcomingLightPreview() {
    AlkaaThemePreview {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleDesign,
                chips = listOf(
                    KuvioTaskChipType.DateSoon(PreviewDateSoon),
                    KuvioTaskChipType.List(PreviewListIb),
                ),
                state = KuvioTaskItemState.PENDING,
                categoryColor = Color(0xFFE07030),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioTaskItemCompletedLightPreview() {
    AlkaaThemePreview {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleInvoice,
                chips = listOf(KuvioTaskChipType.DateLater(PreviewDateYesterday)),
                state = KuvioTaskItemState.COMPLETED,
                categoryColor = Color(0xFF1A6FD4),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioTaskItemOverdueLightPreview() {
    AlkaaThemePreview {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleTax,
                chips = listOf(KuvioTaskChipType.DateToday(PreviewDateYesterday)),
                state = KuvioTaskItemState.OVERDUE,
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioTaskItemWithTagLightPreview() {
    AlkaaThemePreview {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleMeditate,
                chips = listOf(
                    KuvioTaskChipType.Repeat(PreviewRepeatDaily),
                    KuvioTaskChipType.Tag(PreviewTagWellness),
                ),
                state = KuvioTaskItemState.PENDING,
                categoryColor = Color(0xFF34C98C),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

// ── Dark previews ──────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskItemPendingDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitlePresentation,
                chips = listOf(
                    KuvioTaskChipType.DateToday(PreviewDateToday),
                    KuvioTaskChipType.List(PreviewListWork),
                ),
                state = KuvioTaskItemState.PENDING,
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskItemUpcomingDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleDesign,
                chips = listOf(
                    KuvioTaskChipType.DateSoon(PreviewDateSoon),
                    KuvioTaskChipType.List(PreviewListIb),
                ),
                state = KuvioTaskItemState.PENDING,
                categoryColor = Color(0xFFE07030),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskItemCompletedDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleInvoice,
                chips = listOf(KuvioTaskChipType.DateLater(PreviewDateYesterday)),
                state = KuvioTaskItemState.COMPLETED,
                categoryColor = Color(0xFF5BADFF),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskItemOverdueDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleTax,
                chips = listOf(KuvioTaskChipType.DateToday(PreviewDateYesterday)),
                state = KuvioTaskItemState.OVERDUE,
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskItemWithTagDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioTaskItem(
            data = KuvioTaskItemData(
                title = PreviewTitleMeditate,
                chips = listOf(
                    KuvioTaskChipType.Repeat(PreviewRepeatDaily),
                    KuvioTaskChipType.Tag(PreviewTagWellness),
                ),
                state = KuvioTaskItemState.PENDING,
                categoryColor = Color(0xFF34C98C),
            ),
            onItemClick = {},
            onCheckClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
