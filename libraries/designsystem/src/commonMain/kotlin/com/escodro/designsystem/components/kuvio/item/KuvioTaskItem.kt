package com.escodro.designsystem.components.kuvio.item

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

enum class KuvioTaskItemState { Pending, Completed, Overdue }

@Immutable
data class KuvioTaskItemData(
    val title: String,
    val chips: List<com.escodro.designsystem.components.kuvio.chip.KuvioTaskChipType> = emptyList(),
    val state: KuvioTaskItemState = KuvioTaskItemState.Pending,
    val categoryColor: Color? = null,
)

@Composable
fun KuvioTaskItem(
    data: KuvioTaskItemData,
    onItemClick: () -> Unit,
    onCheckClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: implement
}
