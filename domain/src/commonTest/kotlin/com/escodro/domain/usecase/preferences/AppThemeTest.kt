package com.escodro.domain.usecase.preferences

import com.escodro.domain.model.AppThemeOptions
import com.escodro.domain.usecase.fake.PreferencesRepositoryFake
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AppThemeTest {

    private val preferencesRepository = PreferencesRepositoryFake()

    private val updateAppTheme = UpdateAppTheme(preferencesRepository)

    private val loadAppTheme = LoadAppTheme(preferencesRepository)

    @Test
    fun test_if_theme_is_updated() = runTest {
        updateAppTheme(AppThemeOptions.DARK)

        val result = loadAppTheme().first()

        assertEquals(AppThemeOptions.DARK, result)
    }

    @Test
    fun test_if_last_updated_theme_is_the_one_valid() = runTest {
        updateAppTheme(AppThemeOptions.LIGHT)
        updateAppTheme(AppThemeOptions.SYSTEM)
        updateAppTheme(AppThemeOptions.DARK)
        updateAppTheme(AppThemeOptions.LIGHT)
        updateAppTheme(AppThemeOptions.DARK)

        val result = loadAppTheme().first()

        assertEquals(AppThemeOptions.DARK, result)
    }
}
