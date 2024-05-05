package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.module
import com.escodro.alkaa.test.uiTest
import com.escodro.home.presentation.HomeSection
import com.escodro.shared.di.initKoin
import dev.icerock.moko.resources.desc.desc
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class HomeScreenTest {

    private val resourcesProvider = com.escodro.resources.provider.ResourcesProvider()

    @BeforeTest
    fun setup() {
        initKoin(appModule = module)
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun test_titleChangesWhenBottomIconIsSelected() = uiTest {
        HomeSection.entries.forEach { section ->
            val title = resourcesProvider.getString(section.title.desc())

            // Click on each item and validate the title
            onNodeWithContentDescription(label = title, useUnmergedTree = true).performClick()
            onAllNodesWithText(title)[0].assertExists()
        }
    }
}
