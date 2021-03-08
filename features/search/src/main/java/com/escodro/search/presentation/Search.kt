package com.escodro.search.presentation

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
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.search.R
import com.escodro.search.model.TaskSearchItem
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.DefaultIconTextContent
import com.escodro.theme.temp.getViewModel

/**
 * Alkaa Search Section.
 *
 * @param modifier the decorator
 */
@Composable
fun SearchSection(modifier: Modifier = Modifier, onItemClicked: (Long) -> Unit) {
    SearchLoader(modifier = modifier, onItemClicked = onItemClicked)
}

@Composable
private fun SearchLoader(
    modifier: Modifier = Modifier,
    onItemClicked: (Long) -> Unit,
    viewModel: SearchViewModel = getViewModel()
) {
    val (query, setQuery) = rememberSaveable { mutableStateOf("") }
    val viewState by remember(viewModel, query) { viewModel.findTasksByName(query) }
        .collectAsState(initial = SearchViewState.Empty)

    SearchScaffold(
        viewState = viewState,
        modifier = modifier,
        onItemClicked = onItemClicked,
        query = query,
        setQuery = setQuery
    )
}

@Composable
internal fun SearchScaffold(
    modifier: Modifier = Modifier,
    viewState: SearchViewState,
    onItemClicked: (Long) -> Unit,
    query: String,
    setQuery: (String) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SearchTextField(query, onTextChanged = setQuery)

            when (viewState) {
                SearchViewState.Empty -> SearchEmptyContent()
                is SearchViewState.Loaded -> SearchListContent(
                    taskList = viewState.taskList,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@Composable
private fun SearchTextField(text: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = onTextChanged,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search_cd_icon)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    )
}

@Composable
private fun SearchEmptyContent() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ExitToApp,
        iconContentDescription = R.string.search_cd_empty_list,
        header = R.string.search_header_empty
    )
}

@Composable
private fun SearchListContent(taskList: List<TaskSearchItem>, onItemClicked: (Long) -> Unit) {
    LazyColumn {
        items(
            items = taskList,
            itemContent = { task -> SearchItem(task = task, onItemClicked = onItemClicked) }
        )
    }
}

@Composable
private fun SearchItem(task: TaskSearchItem, onItemClicked: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(task.id) },
        verticalArrangement = Arrangement.Center
    ) {

        val textDecoration: TextDecoration
        val circleColor: Color

        if (task.completed) {
            textDecoration = TextDecoration.LineThrough
            circleColor = MaterialTheme.colors.onSecondary
        } else {
            textDecoration = TextDecoration.None
            circleColor = task.categoryColor ?: MaterialTheme.colors.background
        }

        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(circleColor)
            )
            Text(
                text = task.title,
                textDecoration = textDecoration,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
                    .height(24.dp)
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun SearchLoadedListPreview() {
    val task1 = TaskSearchItem(
        completed = true,
        title = "Call Me By Your Name",
        categoryColor = Color.Green,
        isRepeating = false
    )

    val task2 = TaskSearchItem(
        completed = false,
        title = "The Crown",
        categoryColor = Color.White,
        isRepeating = true
    )

    val taskList = listOf(task1, task2)

    AlkaaTheme {
        SearchScaffold(
            modifier = Modifier,
            viewState = SearchViewState.Loaded(taskList),
            onItemClicked = {},
            query = "",
            setQuery = {}
        )
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun SearchEmptyListPreview() {
    AlkaaTheme {
        SearchScaffold(
            modifier = Modifier,
            viewState = SearchViewState.Empty,
            onItemClicked = {},
            query = "",
            setQuery = {}
        )
    }
}
