package com.escodro.designsystem.di

import com.escodro.designsystem.provider.AndroidThemeProvider
import com.escodro.designsystem.provider.ThemeProvider
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformDesignSystemModule = module {
    factoryOf(::AndroidThemeProvider) bind ThemeProvider::class
}
