package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.home.presentation.HomeSection
import com.escodro.resources.provider.ResourcesProvider
import dev.icerock.moko.resources.desc.desc
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest : KoinTest {

    private val resourcesProvider = ResourcesProvider()

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
            val title = resourcesProvider.getString(section.title.desc())

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onAllNodesWithText(title)[0].assertExists()
        }
    }
}
