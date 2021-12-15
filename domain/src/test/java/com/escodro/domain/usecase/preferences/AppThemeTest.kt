package com.escodro.domain.usecase.preferences

import com.escodro.domain.model.AppThemeOptions
import com.escodro.domain.usecase.fake.PreferencesRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppThemeTest {

    private val preferencesRepository = PreferencesRepositoryFake()

    private val updateAppTheme = UpdateAppTheme(preferencesRepository)

    private val loadAppTheme = LoadAppTheme(preferencesRepository)

    @Test
    fun `test if theme is updated`() = runBlockingTest {
        updateAppTheme(AppThemeOptions.DARK)

        val result = loadAppTheme().first()

        Assert.assertEquals(AppThemeOptions.DARK, result)
    }

    @Test
    fun `test if last updated theme is the one valid`() = runBlockingTest {
        updateAppTheme(AppThemeOptions.LIGHT)
        updateAppTheme(AppThemeOptions.SYSTEM)
        updateAppTheme(AppThemeOptions.DARK)
        updateAppTheme(AppThemeOptions.LIGHT)
        updateAppTheme(AppThemeOptions.DARK)

        val result = loadAppTheme().first()

        Assert.assertEquals(AppThemeOptions.DARK, result)
    }
}
