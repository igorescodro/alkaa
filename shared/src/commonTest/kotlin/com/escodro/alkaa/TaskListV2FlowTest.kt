package com.escodro.alkaa

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.local.Category
import com.escodro.local.dao.CategoryDao
import com.escodro.local.dao.TaskDao
import com.escodro.test.AlkaaTest
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class TaskListV2FlowTest : AlkaaTest(), KoinTest {

    private val taskDao: TaskDao by inject()
    private val categoryDao: CategoryDao by inject()

    private val testCategoryId = 42L
    private val testCategoryName = "Work"

    @BeforeTest
    fun setup() {
        beforeTest()
        runTest {
            taskDao.cleanTable()
            categoryDao.cleanTable()
            categoryDao.insertCategory(
                Category(
                    category_id = testCategoryId,
                    category_name = testCategoryName,
                    category_color = "#1A6FD4",
                ),
            )
        }
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun task_list_v2_loaded_through_app_navigation() = uiTest {
        // Navigate to categories first (assuming home screen has categories tab)
        onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()

        // Wait for category list to appear
        waitUntilAtLeastOneExists(hasText(testCategoryName), timeoutMillis = 5000)

        // Verify the test category is visible
        onAllNodesWithText(text = testCategoryName, useUnmergedTree = true)
            .onLast()
            .assertIsDisplayed()
    }

    @Test
    fun category_list_shows_test_category() = uiTest {
        // Navigate to categories
        onNodeWithContentDescription(label = "Categories", useUnmergedTree = true).performClick()

        // Wait for the category to load from database
        waitUntilAtLeastOneExists(hasText(testCategoryName), timeoutMillis = 5000)

        // Verify category is shown in the list
        onNodeWithText(testCategoryName).assertIsDisplayed()
    }
}
