package com.escodro.alkaa.ui.preference

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceFragmentCompat
import com.escodro.alkaa.R
import timber.log.Timber

/**
 * [PreferenceFragmentCompat] containing the application preferences.
 */
class PreferenceFragment : PreferenceFragmentCompat() {

    private var navigator: NavController? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Timber.d("onCreatePreferences()")

        setPreferencesFromResource(R.xml.preferences, rootKey)

        val aboutPref = findPreference(getString(R.string.key_pref_about))
        aboutPref.setOnPreferenceClickListener {
            navigator?.navigate(R.id.key_action_open_about)
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = NavHostFragment.findNavController(this)
    }
}
