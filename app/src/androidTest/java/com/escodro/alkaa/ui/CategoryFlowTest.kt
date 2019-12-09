package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.presentation.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Test class to validate the Category screen and flow.
 */
class CategoryFlowTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Before
    fun navigateToTestScreen() {
        daoProvider.getCategoryDao().cleanTable().blockingGet()
        navigateToCategoryScreen()
    }

    @After
    fun cleanTable() {
        daoProvider.getCategoryDao().cleanTable().blockingGet()
    }

    @Test
    fun areAllViewsIsCompletelyDisplayed() {
        checkThat.viewIsCompletelyDisplayed(R.id.button_categorylist_add)
        checkThat.viewIsCompletelyDisplayed(R.id.textview_categorylist_empty)
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
        events.clickOnViewWithText(R.string.category_list_dialog_remove_positive)
        checkThat.listNotContainsItem(R.id.recyclerview_categorylist_list, categoryName)
        checkThat.viewIsCompletelyDisplayed(R.id.textview_categorylist_empty)
    }

    @Test
    fun cancelDeleteCategory() {
        val categoryName = "Very important!"
        addCategory(categoryName)
        events.clickOnView(R.id.imageview_itemcategory_options)
        events.clickOnViewWithText(R.string.category_list_menu_remove)
        events.clickOnViewWithText(R.string.category_list_dialog_remove_negative)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryName)
    }

    @Test
    fun validateColorChange() {
        val categoryName = "Color rush"
        events.clickOnView(R.id.button_categorylist_add)
        events.textOnView(R.id.edittext_categorynew_description, categoryName)
        events.clickOnView(R.id.radiobutton_categorynew_orange)
        events.clickOnView(R.id.radiobutton_categorynew_green)
        events.clickOnView(R.id.radiobutton_categorynew_yellow)
        events.clickOnView(R.id.radiobutton_categorynew_pink)
        checkThat.viewHasBackgroundColor(R.id.button_categorynew_add, R.color.pink)
    }

    @Test
    fun openCategoryMultipleTimes() {
        navigateToCategoryScreen()
        addCategory("Music")
        events.navigateUp()
        navigateToCategoryScreen()
        addCategory("Books")
        events.navigateUp()
        navigateToCategoryScreen()
        addCategory("Movies")
        events.navigateUp()
        navigateToCategoryScreen()
    }

    @Test
    fun updateCategory() {
        val categoryUpdated = "Adult content!"
        addCategory("Pom")
        events.clickOnView(R.id.imageview_itemcategory_options)
        events.clickOnViewWithText(R.string.category_list_menu_edit)
        events.textOnView(R.id.edittext_categorynew_description, categoryUpdated)
        events.clickOnView(R.id.radiobutton_categorynew_red)
        events.clickOnView(R.id.button_categorynew_add)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryUpdated)
    }

    @Test
    fun updateEmbeddedCategory() {
        val categoryUpdated = "ML cart"
        addDefaultCategory()
        events.clickOnView(R.id.imageview_itemcategory_options)
        events.clickOnViewWithText(R.string.category_list_menu_edit)
        checkThat.viewIsChecked(R.id.radiobutton_categorynew_orange)
        events.textOnView(R.id.edittext_categorynew_description, categoryUpdated)
        events.clickOnView(R.id.radiobutton_categorynew_yellow)
        events.clickOnView(R.id.button_categorynew_add)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryUpdated)
    }

    private fun navigateToCategoryScreen() {
        openDrawer()
        events.clickOnViewWithText(R.string.drawer_menu_manage_categories)
        checkThat.viewHasText(R.id.toolbar_title, R.string.category_list_label)
    }

    private fun addCategory(categoryName: String) {
        events.clickOnView(R.id.button_categorylist_add)
        events.textOnView(R.id.edittext_categorynew_description, categoryName)
        events.clickOnView(R.id.button_categorynew_add)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryName)
    }

    private fun addDefaultCategory() {
        val categoryName = context.getString(R.string.category_default_shopping)
        events.clickOnView(R.id.button_categorylist_add)
        events.textOnView(R.id.edittext_categorynew_description, categoryName)
        events.clickOnView(R.id.radiobutton_categorynew_orange)
        events.clickOnView(R.id.button_categorynew_add)
        checkThat.listContainsItem(R.id.recyclerview_categorylist_list, categoryName)
    }

    private fun openDrawer() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
    }
}
