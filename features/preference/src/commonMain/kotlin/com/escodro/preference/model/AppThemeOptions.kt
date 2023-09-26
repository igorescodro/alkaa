package com.escodro.preference.model

import com.escodro.resources.MR
import dev.icerock.moko.resources.StringResource

/**
 * Enum to represent the app theme selected by the user.
 *
 * @property id the theme id
 * @property titleRes the string title resource
 */
enum class AppThemeOptions(val id: Int, val titleRes: StringResource) {

    /**
     * Light app theme.
     */
    LIGHT(id = 0, titleRes = MR.strings.preference_light_theme),

    /**
     * Dark app theme.
     */
    DARK(id = 1, titleRes = MR.strings.preference_dark_theme),

    /**
     * System-based app theme.
     */
    SYSTEM(id = 2, titleRes = MR.strings.preference_system_default_theme),
}
