package com.escodro.alkaa.ui

import com.escodro.alkaa.R
import com.escodro.alkaa.framework.AcceptanceTest
import com.escodro.alkaa.ui.main.MainActivity
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
        events.clickOnView(R.id.key_action_open_preference)
        checkThat.viewHasText(R.id.toolbar_title, R.string.preference_title)
    }
}
