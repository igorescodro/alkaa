package com.escodro.alkaa.ui

import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class to validate the Category screen and flow.
 */
class CategoryFlowTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun navigateToCategoryScreen() {
        daoProvider.getCategoryDao().cleanTable()
        openActionBarOverflowOrOptionsMenu(context)
        events.clickOnViewWithText(R.string.task_menu_category)
        checkThat.toolbarContainsTitle(R.id.toolbar_main_toolbar, R.string.category_list_label)
    }

    @After
    fun cleanTable() {
        daoProvider.getCategoryDao().cleanTable()
    }

    @Test
    fun areAllViewsIsCompletelyDisplayed() {
        checkThat.viewIsCompletelyDisplayed(R.id.button_categorylist_add)
        checkThat.viewIsCompletelyDisplayed(R.id.recyclerview_categorylist_list)
    }

    @Test
    fun isDescriptionSingleLine() {
        addCategory(
            "Lorem ipsum dolor sit amet, te elit possit suavitate duo. Nec sale sonet" +
                " scriptorem ei, option prompta ut sed. At everti discere oportere sea."
        )
        checkThat.textHasFixedLines(R.id.textview_itemcategory_description, 1)
    }

    @Test
    fun addCategory() {
        addCategory("Work")
    }

    @Test
    fun deleteCategory() {
        val categoryName = "NSFW"
        addCategory(categoryName)
        events.clickOnView(R.id.imageview_itemcategory_options)
        events.clickOnViewWithText(R.string.category_list_menu_remove)
        checkThat.listNotContainsItem(R.id.recyclerview_categorylist_list, categoryName)
    }

    private fun addCategory(categoryName: String) {
        events.clickOnView(R.id.button_categorylist_add)
        events.textOnView(R.id.edittext_categorynew_description, categoryName)
        events.clickOnView(R.id.button_categorynew_add)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryName)
    }
}
