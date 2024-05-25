package com.escodro.preference.model

import com.escodro.resources.Res
import com.escodro.resources.preference_dark_theme
import com.escodro.resources.preference_light_theme
import com.escodro.resources.preference_system_default_theme
import org.jetbrains.compose.resources.StringResource

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
    LIGHT(id = 0, titleRes = Res.string.preference_light_theme),

    /**
     * Dark app theme.
     */
    DARK(id = 1, titleRes = Res.string.preference_dark_theme),

    /**
     * System-based app theme.
     */
    SYSTEM(id = 2, titleRes = Res.string.preference_system_default_theme),
}
