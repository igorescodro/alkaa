package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.presentation.MainActivity
import org.junit.Test

class PreferenceFlowTest : AcceptanceTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun openPreferences() {
        navigateToPreference()
    }

    @Test
    fun openAbout() {
        navigateToPreference()
        events.clickOnViewWithText(R.string.preference_item_about)
        checkThat.viewHasText(R.id.toolbar_title, R.string.about_detail_title)
    }

    private fun navigateToPreference() {
        events.openDrawer(R.id.drawer_layout_main_parent)
        checkThat.drawerIsOpen(R.id.drawer_layout_main_parent)
        events.clickOnViewWithText(R.string.drawer_menu_preferences)
        checkThat.viewHasText(R.id.toolbar_title, R.string.preference_title)
    }
}
