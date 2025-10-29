package com.escodro.search.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.components.content.DefaultIconTextContent
import com.escodro.resources.Res
import com.escodro.resources.search_cd_empty_list
import com.escodro.resources.search_cd_icon
import com.escodro.resources.search_header_empty
import com.escodro.resources.task_detail_pane_title
import com.escodro.resources.task_list_cd_error
import com.escodro.search.model.TaskSearchItem
import com.escodro.taskapi.TaskDetailScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Search Section.
 *
 * @param modifier the decorator
 */
@Composable
fun SearchSection(
    isSinglePane: Boolean,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    SearchLoader(
        isSinglePane = isSinglePane,
        modifier = modifier,
        onItemClick = onItemClick,
    )
}

@Composable
private fun SearchLoader(
    isSinglePane: Boolean,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinInject(),
    taskDetailScreen: TaskDetailScreen = koinInject(),
) {
    val (query, setQuery) = rememberSaveable { mutableStateOf("") }
    val viewState by remember(viewModel, query) {
        viewModel.findTasksByName(query)
    }.collectAsState(initial = SearchViewState.Loading)

    if (isSinglePane) {
        SearchScaffold(
            viewState = viewState,
            modifier = modifier,
            onItemClick = onItemClick,
            query = query,
            setQuery = setQuery,
        )
    } else {
        AdaptiveSearchScaffold(
            taskDetailScreen = { taskId, onUpPress ->
                taskDetailScreen.Content(
                    taskId = taskId,
                    onUpPress = onUpPress,
                )
            },
            viewState = viewState,
            query = query,
            setQuery = setQuery,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun AdaptiveSearchScaffold(
    viewState: SearchViewState,
    query: String,
    setQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
    taskDetailScreen: @Composable (Long, () -> Unit) -> Unit,
) {
    val navigator: ThreePaneScaffoldNavigator<Long> =
        rememberListDetailPaneScaffoldNavigator<Long>()
    val coroutineScope = rememberCoroutineScope()

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                SearchScaffold(
                    viewState = viewState,
                    modifier = modifier,
                    onItemClick = {
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, it)
                        }
                    },
                    query = query,
                    setQuery = setQuery,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val taskId = navigator.currentDestination?.contentKey
                if (taskId != null) {
                    taskDetailScreen(taskId) {
                        coroutineScope.launch {
                            navigator.navigateBack()
                        }
                    }
                } else {
                    DefaultIconTextContent(
                        icon = Icons.Outlined.CheckCircle,
                        iconContentDescription = stringResource(Res.string.task_list_cd_error),
                        header = stringResource(Res.string.task_detail_pane_title),
                    )
                }
            }
        },
    )
}

@Composable
internal fun SearchScaffold(
    viewState: SearchViewState,
    onItemClick: (Long) -> Unit,
    query: String,
    setQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
        ) {
            SearchTextField(query, onTextChange = setQuery)

            Crossfade(viewState) { state ->
                when (state) {
                    SearchViewState.Loading -> AlkaaLoadingContent()
                    SearchViewState.Empty -> SearchEmptyContent()
                    is SearchViewState.Loaded -> SearchListContent(
                        taskList = state.taskList,
                        onItemClick = onItemClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(text: String, onTextChange: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(Res.string.search_cd_icon),
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
    )
}

@Composable
private fun SearchEmptyContent() {
    DefaultIconTextContent(
        icon = Icons.AutoMirrored.Outlined.ExitToApp,
        iconContentDescription = stringResource(Res.string.search_cd_empty_list),
        header = stringResource(Res.string.search_header_empty),
    )
}

@Composable
private fun SearchListContent(
    taskList: ImmutableList<TaskSearchItem>,
    onItemClick: (Long) -> Unit,
) {
    LazyColumn {
        items(
            items = taskList,
            itemContent = { task -> SearchItem(task = task, onItemClick = onItemClick) },
        )
    }
}

@Composable
private fun SearchItem(task: TaskSearchItem, onItemClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable { onItemClick(task.id) },
        verticalArrangement = Arrangement.Center,
    ) {
        val textDecoration: TextDecoration
        val circleColor: Color

        if (task.completed) {
            textDecoration = TextDecoration.LineThrough
            circleColor = MaterialTheme.colorScheme.outline
        } else {
            textDecoration = TextDecoration.None
            circleColor = task.categoryColor ?: MaterialTheme.colorScheme.surfaceVariant
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(circleColor),
            )
            Text(
                text = task.title,
                textDecoration = textDecoration,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(24.dp),
            )
        }
    }
}
