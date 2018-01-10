package com.escodro.alkaa.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * [Module] exposing the [Application] main attributes.
 *
 * @author Igor Escodro on 1/10/18.
 */
@Module
class AndroidModule(
        private val application: Application
) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context =
            application.applicationContext
}
