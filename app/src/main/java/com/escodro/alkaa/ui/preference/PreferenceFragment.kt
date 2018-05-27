package com.escodro.alkaa.ui.preference

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.escodro.alkaa.R

/**
 * [PreferenceFragmentCompat] containing the application preferences.
 *
 * @author Igor Escodro on 27/5/18.
 */
class PreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
