package com.escodro.navigation.di

import com.escodro.navigation.controller.NavEventControllerImpl
import com.escodro.navigation.provider.NavGraphProvider
import com.escodro.navigationapi.controller.NavEventController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val navigationModule = module {

    singleOf(::NavEventControllerImpl) bind NavEventController::class
    single { NavGraphProvider(get(), getAll()) }
}
