//package com.escodro.task
//
//import androidx.compose.ui.test.ComposeUiTest
//import androidx.compose.ui.test.ExperimentalTestApi
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.runComposeUiTest
//import com.escodro.resources.Res
//import com.escodro.resources.task_detail_alarm_no_alarm
//import com.escodro.task.fake.PermissionControllerFake
//import com.escodro.task.presentation.detail.alarm.AlarmSelection
//import com.escodro.task.presentation.detail.alarm.AlarmSelectionContent
//import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
//import com.escodro.task.presentation.detail.alarm.interactor.OpenAlarmSchedulerImpl
//import dev.icerock.moko.permissions.PermissionState
//import kotlinx.coroutines.runBlocking
//import org.jetbrains.compose.resources.getString
//import kotlin.test.AfterTest
//import kotlin.test.Test
//import kotlin.test.assertFalse
//import kotlin.test.assertTrue
//
//@OptIn(ExperimentalTestApi::class)
//internal class AlarmPermissionFlowTest {
//
//    private val permissionsController = PermissionControllerFake()
//
//    private val state = AlarmSelectionState(
//        calendar = null,
//        alarmInterval = null,
//        permissionsController = permissionsController
//    )
//
//    @AfterTest
//    fun tearDown() {
//        permissionsController.clean()
//    }
//
//    @Test
//    fun test_notificationPermissionGrantedDoesNotShowDialog() = runComposeUiTest {
//        permissionsController.isPermissionGrantedValue = true
//        permissionsController.permissionStateValue = PermissionState.Granted
//
//        loadAlarmSelectionContent(
//            state = state,
//            hasAlarmPermission = true,
//        )
//
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        assertFalse(state.showNotificationDialog)
//        assertFalse(state.showRationaleDialog)
//        assertFalse(state.showExactAlarmDialog)
//    }
//
//    @Test
//    fun test_noNotificationPermissionShowsPermissionDialog() = runComposeUiTest {
//        permissionsController.isPermissionGrantedValue = false
//        permissionsController.permissionStateValue = PermissionState.NotGranted
//
//        loadAlarmSelectionContent(
//            state = state,
//            hasAlarmPermission = true,
//        )
//
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        assertTrue(state.showNotificationDialog)
//        assertFalse(state.showRationaleDialog)
//        assertFalse(state.showExactAlarmDialog)
//    }
//
//    @Test
//    fun test_noNotificationAndShowPermissionRationaleDialog() = runComposeUiTest {
//        permissionsController.isPermissionGrantedValue = false
//        permissionsController.permissionStateValue = PermissionState.Denied
//        permissionsController.throwsException = true
//
//        loadAlarmSelectionContent(
//            state = state,
//            hasAlarmPermission = true,
//        )
//
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        assertFalse(state.showNotificationDialog)
//        assertTrue(state.showRationaleDialog)
//        assertFalse(state.showExactAlarmDialog)
//    }
//
//    @Test
//    fun test_noExactAlarmPermissionDialog() = runComposeUiTest {
//        permissionsController.permissionStateValue = PermissionState.NotDetermined
//
//        loadAlarmSelectionContent(
//            state = state,
//            hasAlarmPermission = false,
//        )
//
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        assertFalse(state.showNotificationDialog)
//        assertFalse(state.showRationaleDialog)
//        assertTrue(state.showExactAlarmDialog)
//    }
//
//    @Test
//    fun test_noPermissionAtAllDialog() = runComposeUiTest {
//        permissionsController.isPermissionGrantedValue = false
//        val permissionState = PermissionState.Denied
//
//        loadAlarmSelectionContent(
//            state = state,
//            hasAlarmPermission = false,
//        )
//
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        assertTrue(state.showNotificationDialog)
//        assertFalse(state.showRationaleDialog)
//        assertTrue(state.showExactAlarmDialog)
//    }
//
//    private fun ComposeUiTest.loadAlarmSelectionContent(
//        state: AlarmSelectionState,
//        hasAlarmPermission: Boolean,
//    ) {
//        setContent {
//            AlarmSelectionContent(
//                alarmSelectionState = state,
//                hasExactAlarmPermission = { hasAlarmPermission },
//                onAlarmUpdate = {},
//                onIntervalSelect = {},
//                openAlarmScheduler = OpenAlarmSchedulerImpl()
//            )
//        }
//    }
//}
