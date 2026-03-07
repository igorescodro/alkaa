package com.escodro.designsystem.components.kuvio.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import com.escodro.designsystem.components.kuvio.chip.KuvioTaskChipType

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
        cardBackground = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
        titleColor = MaterialTheme.colorScheme.onSurface,
        titleDecoration = TextDecoration.None,
    )
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
    // TODO: implement
}
