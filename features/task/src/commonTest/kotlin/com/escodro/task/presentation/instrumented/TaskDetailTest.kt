package com.escodro.task.presentation.instrumented

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.permission.api.PermissionController
import com.escodro.resources.Res
import com.escodro.resources.task_detail_cd_error
import com.escodro.resources.task_detail_header_error
import com.escodro.task.model.Task
import com.escodro.task.presentation.detail.TaskDetailActions
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmScheduler
import com.escodro.task.presentation.detail.main.TaskDetailRouter
import com.escodro.task.presentation.detail.main.TaskDetailState
import com.escodro.task.presentation.fake.OpenAlarmSchedulerFake
import com.escodro.task.presentation.fake.PermissionControllerFake
import com.escodro.test.AlkaaTest
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.compose.KoinApplication
import org.koin.dsl.module
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskDetailTest : AlkaaTest() {

    private val testModule = module {
        single<PermissionController> { PermissionControllerFake() }
        single<OpenAlarmScheduler> { OpenAlarmSchedulerFake() }
    }

    @Test
    fun test_errorViewIsShown() = runComposeUiTest {
        // Given an error state
        val state = TaskDetailState.Error

        // When the view is loaded
        loadTaskDetail(state)

        // Assert that the error view is loaded
        val header = runBlocking { getString(Res.string.task_detail_header_error) }
        val contentDescription = runBlocking { getString(Res.string.task_detail_cd_error) }
        onNodeWithText(text = header).assertExists()
        onNodeWithContentDescription(label = contentDescription).assertExists()
    }

    @Test
    fun test_detailContentIsShown() = runComposeUiTest {
        // Given a success state
        val task = Task(
            title = "Call John",
            description = "I can't forget his birthday again",
            dueDate = null,
            categoryId = 10L,
        )
        val state = TaskDetailState.Loaded(task)

        // When the view is loaded
        loadTaskDetail(state)

        // Assert that the task content is shown
        onNodeWithText(text = task.title).assertExists()
        onNodeWithText(text = task.description!!).assertExists()
    }

    private fun ComposeUiTest.loadTaskDetail(state: TaskDetailState) = setContent {
        KoinApplication(application = { modules(testModule) }) {
            AlkaaThemePreview {
                TaskDetailRouter(
                    isSinglePane = true,
                    detailViewState = state,
                    categoryViewState = CategoryState.Loaded(persistentListOf()),
                    actions = TaskDetailActions(),
                )
            }
        }
    }
}
