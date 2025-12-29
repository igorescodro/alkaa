package com.escodro.task.presentation

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.task_detail_alarm_no_alarm
import com.escodro.task.presentation.detail.alarm.AlarmSelectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmSchedulerImpl
import com.escodro.task.presentation.fake.PermissionControllerFake
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
internal class AlarmPermissionFlowTest : AlkaaTest() {

    private val permissionsController = PermissionControllerFake()

    private val state = AlarmSelectionState(
        calendar = null,
        alarmInterval = null,
        permissionsController = permissionsController,
    )

    @AfterTest
    fun tearDown() {
        permissionsController.clean()
    }

    @Test
    fun test_notificationPermissionGrantedDoesNotShowDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = true

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = true,
        )

        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
        onNodeWithText(noAlarmString).performClick()

        assertFalse(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertFalse(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noNotificationPermissionShowsPermissionDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = false

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = true,
        )

        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
        onNodeWithText(noAlarmString).performClick()

        assertTrue(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertFalse(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noExactAlarmPermissionDialog() = runComposeUiTest {
        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = false,
        )

        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
        onNodeWithText(noAlarmString).performClick()

        assertFalse(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertTrue(state.isExactAlarmDialogOpen)
    }

    @Test
    fun test_noPermissionAtAllDialog() = runComposeUiTest {
        permissionsController.isPermissionGrantedValue = false

        loadAlarmSelectionContent(
            state = state,
            hasAlarmPermission = false,
        )

        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
        onNodeWithText(noAlarmString).performClick()

        assertTrue(state.isNotificationDialogOpen)
        assertFalse(state.isRationaleDialogOpen)
        assertTrue(state.isExactAlarmDialogOpen)
    }

    private fun ComposeUiTest.loadAlarmSelectionContent(
        state: AlarmSelectionState,
        hasAlarmPermission: Boolean,
    ) {
        setContent {
            AlkaaThemePreview {
                AlarmSelectionContent(
                    alarmSelectionState = state,
                    hasExactAlarmPermission = { hasAlarmPermission },
                    onAlarmUpdate = {},
                    onIntervalSelect = {},
                    openAlarmScheduler = OpenAlarmSchedulerImpl(),
                )
            }
        }
    }
}
