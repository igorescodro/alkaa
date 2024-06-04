//package com.escodro.task
//
//import androidx.activity.compose.setContent
//import androidx.compose.ui.test.ExperimentalTestApi
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.onAllNodesWithText
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.runComposeUiTest
//import com.escodro.designsystem.AlkaaTheme
//import com.escodro.resources.Res
//import com.escodro.resources.task_detail_alarm_no_alarm
//import com.escodro.task.model.AlarmInterval
//import com.escodro.task.presentation.detail.alarm.AlarmSelection
//import com.escodro.test.espresso.Events
//import kotlinx.coroutines.runBlocking
//import org.jetbrains.compose.resources.getString
//import java.util.Calendar
//import kotlin.test.Test
//
//@OptIn(ExperimentalTestApi::class)
//internal class AlarmSelectionTest {
//
//    @Test
//    fun test_addAlarm() = runComposeUiTest {
//        // Load the alarm section component
//        loadAlarmSelection()
//
//        // Click in the alarm item
//        val noAlarmString = runBlocking { getString(Res.string.task_detail_alarm_no_alarm) }
//        onNodeWithText(noAlarmString).performClick()
//
//        // Set alarm to 2021-04-15 - 17:00:00
//        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
//        Events.setDateTime(calendar)
//
//        // Assert that the date is shown in the view
//        onNodeWithText(text = "15", substring = true).assertIsDisplayed()
//        onNodeWithText(text = "2021", substring = true).assertIsDisplayed()
//
//        // Assert that the alarm interval item is shown
//        val intervalNeverString = context.resources.getStringArray(R.array.task_alarm_repeating)[0]
//        composeTestRule.onNodeWithText(intervalNeverString).assertIsDisplayed()
//
//        // Assert that the remove alarm button is shown
//        val removeAlarmCd = context.getString(R.string.task_detail_cd_icon_remove_alarm)
//        composeTestRule.onNodeWithContentDescription(removeAlarmCd, useUnmergedTree = true)
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun test_addAndRemoveAlarm() {
//        // Load the alarm section component
//        loadAlarmSelection()
//
//        // Click in the alarm item
//        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
//        composeTestRule.onNodeWithText(noAlarmString).performClick()
//
//        // Set alarm to 2021-04-15 - 17:00:00
//        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
//        Events.setDateTime(calendar)
//
//        // Click to remove the alarm
//        val removeAlarmCd = context.getString(R.string.task_detail_cd_icon_remove_alarm)
//        composeTestRule.onNodeWithContentDescription(removeAlarmCd, useUnmergedTree = true)
//            .performClick()
//
//        // Assert that the alarm item is not set again
//        composeTestRule.onNodeWithText(noAlarmString).assertIsDisplayed()
//
//        val repeatIconCd = context.getString(R.string.task_detail_cd_icon_repeat_alarm)
//        composeTestRule.onNodeWithText(repeatIconCd).assertDoesNotExist()
//    }
//
//    @Test
//    fun test_allAlarmIntervalsCanBeSelected() {
//        // Load the alarm section component
//        loadAlarmSelection()
//
//        // Click in the alarm item
//        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
//        composeTestRule.onNodeWithText(noAlarmString).performClick()
//
//        // Set alarm to 2021-04-15 - 17:00:00
//        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
//        Events.setDateTime(calendar)
//
//        // Assert that when clicking in each option, it is shown as selected
//        val alarmArray = context.resources.getStringArray(R.array.task_alarm_repeating)
//        val repeatIconCd = context.getString(R.string.task_detail_cd_icon_repeat_alarm)
//        alarmArray.forEach { string ->
//            composeTestRule.onNodeWithContentDescription(repeatIconCd, useUnmergedTree = true)
//                .performClick()
//            composeTestRule.onAllNodesWithText(string)[0].performClick()
//            composeTestRule.onAllNodesWithText(string)[0].assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun test_alarmAlreadySetLoadsShowsCorrectly() {
//        // Load the alarm section with preset values (2021-04-15 - 17:00:00)
//        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
//        val alarmInterval = AlarmInterval.MONTHLY
//        loadAlarmSelection(calendar, alarmInterval)
//
//        val alarmIndex = alarmInterval.index ?: 0
//        val alarmString = context.resources.getStringArray(R.array.task_alarm_repeating)[alarmIndex]
//
//        // Assert that the date is shown in the view
//        composeTestRule.onNodeWithText(text = "15", substring = true).assertIsDisplayed()
//        composeTestRule.onNodeWithText(text = "2021", substring = true).assertIsDisplayed()
//
//        // Assert that the alarm interval is shown in teh view
//        composeTestRule.onNodeWithText(alarmString).assertIsDisplayed()
//    }
//
//    @Test
//    fun test_whenExactAlarmPermissionIsNotGrantedDialogIsShown() {
//        // Load the alarm section component without permission
//        loadAlarmSelection(hasExactAlarmPermission = false)
//
//        // Click in the alarm item
//        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
//        composeTestRule.onNodeWithText(noAlarmString).performClick()
//
//        // Assert that the alarm item is not set again
//        val dialogTitle = context.getString(R.string.task_alarm_permission_dialog_title)
//        composeTestRule.onNodeWithText(dialogTitle).assertIsDisplayed()
//    }
//
//    private fun loadAlarmSelection(
//        hasExactAlarmPermission: Boolean = true,
//        shouldAskForNotificationPermission: Boolean = false,
//    ) {
//        scenario.onActivity { activity ->
//            activity.setContent {
//                AlkaaTheme {
//                    AlarmSelection(
//                        calendar = null,
//                        interval = AlarmInterval.NEVER,
//                        onAlarmUpdate = {},
//                        onIntervalSelect = {},
//                        hasAlarmPermission = { hasExactAlarmPermission },
//                        shouldCheckNotificationPermission = shouldAskForNotificationPermission,
//                    )
//                }
//            }
//        }
//    }
//
//    private fun loadAlarmSelection(
//        calendar: Calendar,
//        alarmInterval: AlarmInterval,
//    ) {
//        scenario.onActivity { activity ->
//            activity.setContent {
//                AlkaaTheme {
//                    AlarmSelection(
//                        calendar = calendar,
//                        interval = alarmInterval,
//                        onAlarmUpdate = {},
//                        onIntervalSelect = {},
//                        hasAlarmPermission = { true },
//                        shouldCheckNotificationPermission = false,
//                    )
//                }
//            }
//        }
//    }
//}
