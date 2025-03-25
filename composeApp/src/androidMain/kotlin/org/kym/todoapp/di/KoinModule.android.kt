package org.kym.todoapp.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.kym.todoapp.platform.AlarmSoundPlayer

actual val platformModule = module {
    singleOf(::AlarmSoundPlayer) // platform specific dependency for playing sound
}