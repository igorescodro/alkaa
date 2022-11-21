package com.escodro.task

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.fake.PermissionStateFake
import com.escodro.task.presentation.detail.alarm.AlarmSelectionContent
import com.escodro.task.presentation.detail.alarm.AlarmSelectionState
import com.escodro.test.rule.DisableAnimationsRule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalPermissionsApi::class)
internal class AlarmPermissionFlowTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private lateinit var scenario: ActivityScenario<ComponentActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(ComponentActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun test_notificationPermissionGrantedDoesNotShowDialog() {
        val state = AlarmSelectionState(calendar = null, alarmInterval = null)
        val permissionState = PermissionStateFake(fakeStatus = PermissionStatus.Granted)

        loadAlarmSelectionContent(
            state = state,
            permissionState = permissionState,
            hasAlarmPermission = true
        )

        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        Assert.assertFalse(state.showNotificationDialog)
        Assert.assertFalse(state.showRationaleDialog)
        Assert.assertFalse(state.showExactAlarmDialog)
    }

    @Test
    fun test_noNotificationPermissionShowsPermissionDialog() {
        val state = AlarmSelectionState(calendar = null, alarmInterval = null)
        val permissionState =
            PermissionStateFake(fakeStatus = PermissionStatus.Denied(shouldShowRationale = false))

        loadAlarmSelectionContent(
            state = state,
            permissionState = permissionState,
            hasAlarmPermission = true
        )

        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        Assert.assertTrue(state.showNotificationDialog)
        Assert.assertFalse(state.showRationaleDialog)
        Assert.assertFalse(state.showExactAlarmDialog)
    }

    @Test
    fun test_noNotificationAndShowPermissionRationaleDialog() {
        val state = AlarmSelectionState(calendar = null, alarmInterval = null)
        val permissionState =
            PermissionStateFake(fakeStatus = PermissionStatus.Denied(shouldShowRationale = true))

        loadAlarmSelectionContent(
            state = state,
            permissionState = permissionState,
            hasAlarmPermission = true
        )

        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        Assert.assertFalse(state.showNotificationDialog)
        Assert.assertTrue(state.showRationaleDialog)
        Assert.assertFalse(state.showExactAlarmDialog)
    }

    @Test
    fun test_noExactAlarmPermissionDialog() {
        val state = AlarmSelectionState(calendar = null, alarmInterval = null)
        val permissionState = PermissionStateFake(fakeStatus = PermissionStatus.Granted)

        loadAlarmSelectionContent(
            state = state,
            permissionState = permissionState,
            hasAlarmPermission = false
        )

        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        Assert.assertFalse(state.showNotificationDialog)
        Assert.assertFalse(state.showRationaleDialog)
        Assert.assertTrue(state.showExactAlarmDialog)
    }

    @Test
    fun test_noPermissionAtAllDialog() {
        val state = AlarmSelectionState(calendar = null, alarmInterval = null)
        val permissionState =
            PermissionStateFake(fakeStatus = PermissionStatus.Denied(shouldShowRationale = false))

        loadAlarmSelectionContent(
            state = state,
            permissionState = permissionState,
            hasAlarmPermission = false
        )

        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        Assert.assertTrue(state.showNotificationDialog)
        Assert.assertFalse(state.showRationaleDialog)
        Assert.assertTrue(state.showExactAlarmDialog)
    }

    private fun loadAlarmSelectionContent(
        state: AlarmSelectionState,
        permissionState: PermissionState,
        hasAlarmPermission: Boolean
    ) {
        scenario.onActivity { activity ->
            activity.setContent {
                AlkaaTheme {
                    AlarmSelectionContent(
                        context = LocalContext.current,
                        state = state,
                        permissionState = permissionState,
                        hasAlarmPermission = { hasAlarmPermission },
                        onAlarmUpdate = {},
                        onIntervalSelect = {}
                    )
                }
            }
        }
    }
}
