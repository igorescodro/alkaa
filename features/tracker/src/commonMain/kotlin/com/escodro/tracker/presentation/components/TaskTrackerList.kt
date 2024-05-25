package com.escodro.tracker.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.escodro.resources.Res
import com.escodro.resources.tracker_cp_item_icon
import com.escodro.resources.tracker_name_no_category
import com.escodro.tracker.model.Tracker
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TaskTrackerList(
    list: ImmutableList<Tracker.CategoryInfo>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(list) { item ->
            TrackerItem(item)
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun TrackerItem(item: Tracker.CategoryInfo) {
    val tint = item.color?.let { Color(it) } ?: MaterialTheme.colorScheme.outline
    val name = item.name ?: stringResource(Res.string.tracker_name_no_category)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = stringResource(Res.string.tracker_cp_item_icon),
            tint = tint,
            modifier = Modifier
                .size(32.dp)
                .weight(1F),
        )
        Text(text = name, modifier = Modifier.weight(4F))
        Text(
            text = item.taskCount.toString(),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1F),
        )
    }
}
