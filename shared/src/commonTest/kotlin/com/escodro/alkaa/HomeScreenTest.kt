package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.uiTest
import com.escodro.home.presentation.HomeSection
import dev.icerock.moko.resources.desc.desc
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest {

    private val resourcesProvider = com.escodro.resources.provider.ResourcesProvider()

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun test_titleChangesWhenBottomIconIsSelected() = uiTest {
        HomeSection.entries.forEach { section ->
            val title = resourcesProvider.getString(section.title.desc())

            // Click on each item and validate the title
            onNodeWithText(text = title, useUnmergedTree = true).performClick()
            onAllNodesWithText(title)[0].assertExists()
        }
    }
}
