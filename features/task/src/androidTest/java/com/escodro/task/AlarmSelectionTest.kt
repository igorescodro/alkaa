package com.escodro.task

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithSubstring
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.task.espresso.setDateTime
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.alarm.AlarmSelection
import com.escodro.theme.AlkaaTheme
import org.junit.Rule
import org.junit.Test
import java.util.Calendar

internal class AlarmSelectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_addAlarm() {
        // Load the alarm section component
        loadAlarmSelection()

        // Click in the alarm item
        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        // Set alarm to 2021-04-15 - 17:00:00
        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
        setDateTime(calendar)

        // Assert that the date is shown in the view
        composeTestRule.onNodeWithSubstring("15").assertIsDisplayed()
        composeTestRule.onNodeWithSubstring("2021").assertIsDisplayed()

        // Assert that the alarm interval item is shown
        val intervalNeverString = context.resources.getStringArray(R.array.task_alarm_repeating)[0]
        composeTestRule.onNodeWithText(intervalNeverString).assertIsDisplayed()

        // Assert that the remove alarm button is shown
        val removeAlarmCd = context.getString(R.string.task_detail_cd_icon_remove_alarm)
        composeTestRule.onNodeWithContentDescription(removeAlarmCd).assertIsDisplayed()
    }

    @Test
    fun test_addAndRemoveAlarm() {
        // Load the alarm section component
        loadAlarmSelection()

        // Click in the alarm item
        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        // Set alarm to 2021-04-15 - 17:00:00
        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
        setDateTime(calendar)

        // Click to remove the alarm
        val removeAlarmCd = context.getString(R.string.task_detail_cd_icon_remove_alarm)
        composeTestRule.onNodeWithContentDescription(removeAlarmCd).performClick()

        // Assert that the alarm item is not set again
        composeTestRule.onNodeWithText(noAlarmString).assertIsDisplayed()

        val repeatIconCd = context.getString(R.string.task_detail_cd_icon_repeat_alarm)
        composeTestRule.onNodeWithText(repeatIconCd).assertDoesNotExist()
    }

    @Test
    fun test_allAlarmIntervalsCanBeSelected() {
        // Load the alarm section component
        loadAlarmSelection()

        // Click in the alarm item
        val noAlarmString = context.getString(R.string.task_detail_alarm_no_alarm)
        composeTestRule.onNodeWithText(noAlarmString).performClick()

        // Set alarm to 2021-04-15 - 17:00:00
        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
        setDateTime(calendar)

        // Assert that when clicking in each option, it is shown as selected
        val alarmArray = context.resources.getStringArray(R.array.task_alarm_repeating)
        val repeatIconCd = context.getString(R.string.task_detail_cd_icon_repeat_alarm)
        alarmArray.forEach { string ->
            composeTestRule.onNodeWithContentDescription(repeatIconCd).performClick()
            composeTestRule.onAllNodesWithText(string)[0].performClick()
            composeTestRule.onAllNodesWithText(string)[0].assertIsDisplayed()
        }
    }

    @Test
    fun test_alarmAlreadySetLoadsShowsCorrectly() {
        // Load the alarm section with preset values (2021-04-15 - 17:00:00)
        val calendar = Calendar.getInstance().apply { timeInMillis = 1_618_516_800_000 }
        val alarmInterval = AlarmInterval.MONTHLY
        loadAlarmSelection(calendar, alarmInterval)

        val alarmIndex = alarmInterval.index ?: 0
        val alarmString = context.resources.getStringArray(R.array.task_alarm_repeating)[alarmIndex]

        // Assert that the date is shown in the view
        composeTestRule.onNodeWithSubstring("15").assertIsDisplayed()
        composeTestRule.onNodeWithSubstring("2021").assertIsDisplayed()

        // Assert that the alarm interval is shown in teh view
        composeTestRule.onNodeWithText(alarmString).assertIsDisplayed()
    }

    private fun loadAlarmSelection() {
        composeTestRule.setContent {
            AlkaaTheme {
                AlarmSelection(
                    calendar = null,
                    interval = AlarmInterval.NEVER,
                    onAlarmUpdated = {},
                    onIntervalSelected = {}
                )
            }
        }
    }

    private fun loadAlarmSelection(calendar: Calendar, alarmInterval: AlarmInterval) {
        composeTestRule.setContent {
            AlkaaTheme {
                AlarmSelection(
                    calendar = calendar,
                    interval = alarmInterval,
                    onAlarmUpdated = {},
                    onIntervalSelected = {}
                )
            }
        }
    }
}
