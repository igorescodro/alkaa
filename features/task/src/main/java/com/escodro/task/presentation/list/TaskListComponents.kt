package com.escodro.task.presentation.list

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import java.util.Calendar

/**
 * Alkaa Task Item.
 *
 * @param task the task item to be rendered
 * @param onItemClick the action to be done when the item is clicked
 * @param onCheckedChange action to be called when the checked value changes
 * @param modifier the decorator
 */
@Composable
internal fun TaskItem(
    task: TaskWithCategory,
    onItemClick: (Long) -> Unit,
    onCheckedChange: (TaskWithCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .height(74.dp)
            .clickable { onItemClick(task.task.id) },
    ) {
        Row {
            CardRibbon(colorInt = task.category?.color)
            RadioButton(
                modifier = Modifier
                    .fillMaxHeight()
                    .semantics { checkboxName = task.task.title },
                selected = task.task.completed,
                onClick = { onCheckedChange(task) },
            )
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = task.task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
                RelativeDateText(calendar = task.task.dueDate)
            }
        }
    }
}

@Composable
internal fun CardRibbon(colorInt: Int?, modifier: Modifier = Modifier) {
    val ribbonColor = if (colorInt != null) {
        Color(colorInt)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Spacer(
        modifier
            .width(18.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .background(ribbonColor),
    )
}

@Composable
internal fun RelativeDateText(calendar: Calendar?) {
    if (calendar == null) {
        return
    }

    val context = LocalContext.current
    val time = calendar.time.time
    val stringTime = DateUtils
        .getRelativeDateTimeString(
            context,
            time,
            DateUtils.DAY_IN_MILLIS,
            DateUtils.DAY_IN_MILLIS,
            0,
        )
        .toString()

    Text(
        text = stringTime,
        style = MaterialTheme.typography.bodySmall,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
    )
}

/**
 * Semantics key for Checkbox naming.
 */
val CheckboxNameKey = SemanticsPropertyKey<String>("Checkbox")
private var SemanticsPropertyReceiver.checkboxName by CheckboxNameKey

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskItemPreview() {
    val task1 = Task(title = "Buy milk", dueDate = null)
    val category1 = Category(name = "Books", color = android.graphics.Color.BLUE)
    val taskWithCategory = TaskWithCategory(task = task1, category = category1)

    TaskItem(
        modifier = Modifier,
        task = taskWithCategory,
        onCheckedChange = {},
        onItemClick = {},
    )
}
