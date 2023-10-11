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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.designsystem.components.DefaultIconTextContent
import com.escodro.resources.MR
import com.escodro.tracker.di.injectDynamicFeature
import com.escodro.tracker.model.Tracker
import com.escodro.tracker.presentation.components.TaskGraph
import com.escodro.tracker.presentation.components.TaskTrackerList
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import org.koin.compose.koinInject

@Composable
fun TrackerScreen(onUpPress: () -> Unit) {
    injectDynamicFeature()
    TrackerLoader(onUpPress = onUpPress)
}

@Composable
internal fun TrackerLoader(viewModel: TrackerViewModel = koinInject(), onUpPress: () -> Unit) {
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
        iconContentDescription = stringResource(MR.strings.tracker_cd_empty),
        header = stringResource(MR.strings.tracker_header_empty),
        modifier = Modifier.padding(16.dp),
    )
}

@Composable
private fun TrackerError() {
    DefaultIconTextContent(
        icon = Icons.Outlined.Close,
        iconContentDescription = stringResource(MR.strings.tracker_cd_error),
        header = stringResource(MR.strings.tracker_header_error),
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

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Default.DynamicFeed,
                contentDescription = stringResource(MR.strings.tracker_cp_info_icon),
                modifier = Modifier
                    .weight(1F)
                    .size(36.dp),
            )
            Column(modifier = Modifier.weight(3F)) {
                Text(
                    text = stringResource(
                        resource = MR.plurals.tracker_message_title,
                        quantity = taskCount,
                        taskCount,
                    ),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(MR.strings.tracker_message_description),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
