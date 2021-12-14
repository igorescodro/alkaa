package com.escodro.domain.usecase.preferences

import com.escodro.domain.model.AppThemeOptions

/**
 * Updates the current app theme.
 *
 * @property preferencesRepository the preferences repository
 */
class UpdateAppTheme(private val preferencesRepository: PreferencesRepository) {

    /**
     * Updates the current app theme.
     *
     * @param theme the theme to be updated
     */
    suspend operator fun invoke(theme: AppThemeOptions) =
        preferencesRepository.updateAppTheme(theme)
}
