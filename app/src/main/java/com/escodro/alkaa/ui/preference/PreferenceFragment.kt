package com.escodro.alkaa.ui.preference

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.escodro.alkaa.R

/**
 * [PreferenceFragmentCompat] containing the application preferences.
 */
class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
