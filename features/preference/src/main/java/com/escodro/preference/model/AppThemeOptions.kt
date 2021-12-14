package com.escodro.preference.model

import androidx.annotation.StringRes
import com.escodro.preference.R

/**
 * Enum to represent the app theme selected by the user.
 *
 * @property id the theme id
 * @property titleRes the string title resource
 */
enum class AppThemeOptions(val id: Int, @StringRes val titleRes: Int) {

    /**
     * Light app theme.
     */
    LIGHT(0, R.string.preference_light_theme),

    /**
     * Dark app theme.
     */
    DARK(1, R.string.preference_dark_theme),

    /**
     * System-based app theme.
     */
    SYSTEM(2, R.string.preference_system_default_theme)
}
