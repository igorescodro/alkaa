package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.home.presentation.HomeSection
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest : KoinTest {

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
        HomeSection.entries.forEach { section ->
            val title = runBlocking { getString(section.title) }

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onAllNodesWithText(title)[0].assertExists()
        }
    }
}
