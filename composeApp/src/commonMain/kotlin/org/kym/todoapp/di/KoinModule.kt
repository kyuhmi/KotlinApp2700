package org.kym.todoapp.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.kym.todoapp.data.UserRepository
import org.kym.todoapp.data.UserRepositoryImpl
import org.kym.todoapp.viewModels.PomodoroViewModel
import org.kym.todoapp.viewModels.TestViewModel

// platform specific implementation expected in platform specific packages
expect val platformModule: Module

// common modules that all targets will share
val commonModule = module {
    singleOf<UserRepository>(::UserRepositoryImpl)
    viewModelOf(::TestViewModel)

    // pomodoro
    viewModelOf(::PomodoroViewModel)
}

// Function to start Koin.  Platform-specific code will call this.
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(commonModule, platformModule)
    }
}