package com.escodro.tracker.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DataUsage
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.tracker.R
import com.escodro.tracker.model.Tracker
import com.escodro.tracker.presentation.components.TaskGraph
import com.escodro.tracker.presentation.components.TaskTrackerList
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.getViewModel

@Composable
internal fun TrackerSection(onUpPress: () -> Unit) {
    TrackerLoader(onUpPress = onUpPress)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun TrackerLoader(viewModel: TrackerViewModel = getViewModel(), onUpPress: () -> Unit) {
    val data by remember {
        viewModel.loadTracker()
    }.collectAsState(initial = TrackerViewState.Loading)

    Scaffold(topBar = { AlkaaToolbar(onUpPress = onUpPress) }) { paddingValues ->
        Crossfade(targetState = data, modifier = Modifier.padding(paddingValues)) { state ->
            when (state) {
                TrackerViewState.Empty -> TrackerEmpty()
                is TrackerViewState.Error -> TrackerError()
                is TrackerViewState.Loaded -> TrackerLoadedContent(state.trackerInfo)
                TrackerViewState.Loading -> AlkaaLoadingContent()
            }
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun TrackerLoadedContent(trackerInfo: Tracker.Info) {
    val categoryList = trackerInfo.categoryInfoList
    Column {
        TaskGraph(
            list = categoryList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(3F)
                .padding(24.dp),
        )
        TaskTrackerList(
            list = categoryList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2F),
        )
        TaskTrackerInfoCard(
            list = categoryList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(24.dp),
        )
    }
}

@Composable
private fun TrackerEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.DataUsage,
        iconContentDescription = R.string.tracker_cd_empty,
        header = R.string.tracker_header_empty,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun TrackerError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = R.string.tracker_cd_error,
        header = R.string.tracker_header_error,
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
@Suppress("MagicNumber")
private fun TaskTrackerInfoCard(
    list: ImmutableList<Tracker.CategoryInfo>,
    modifier: Modifier = Modifier,
) {
    val taskCount = list.sumOf { item -> item.taskCount }
    val message = LocalContext.current.resources
        .getQuantityString(R.plurals.tracker_message_title, taskCount, taskCount)

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.DynamicFeed,
                contentDescription = stringResource(id = R.string.tracker_cp_info_icon),
                modifier = Modifier
                    .weight(1F)
                    .size(36.dp),
            )
            Column(modifier = Modifier.weight(3F)) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(id = R.string.tracker_message_description),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
