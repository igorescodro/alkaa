package com.escodro.alkaa

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import com.escodro.alkaa.test.AlkaaBaseTest
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TopLevelDestinations
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest : AlkaaBaseTest(), KoinTest {

    @BeforeTest
    fun setup() {
        beforeTest()
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun when_tab_changes_then_title_updates() = uiTest {
        TopLevelDestinations.forEach { section ->
            val title = runBlocking { getString(section.title) }

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onNodeWithTag(section.title.toString()).assertIsSelected()
        }
    }

    @Test
    fun when_tab_changes_and_back_pressed_then_title_updates() = uiTest {
        // Click on Settings tab
        val settingsTitle = runBlocking { getString(HomeDestination.Preferences.title) }
        onNodeWithContentDescription(label = settingsTitle, useUnmergedTree = true).performClick()
        onNodeWithTag(HomeDestination.Preferences.title.toString()).assertIsSelected()

        // Press back button
        onAllNodes(isRoot())[0].performKeyInput {
            keyDown(Key.Back)
            keyUp(Key.Back)
        }

        // Click on Tasks tab
        val tasksTitle = runBlocking { getString(HomeDestination.TaskList.title) }
        onNodeWithContentDescription(label = tasksTitle, useUnmergedTree = true).performClick()
        onNodeWithTag(HomeDestination.TaskList.title.toString()).assertIsSelected()
    }
}
