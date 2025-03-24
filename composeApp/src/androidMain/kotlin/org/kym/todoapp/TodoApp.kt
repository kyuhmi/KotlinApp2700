package org.kym.todoapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.kym.todoapp.di.initKoin

class TodoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // extend koin init to include android context (add other platform specific dependencies here?)
        initKoin {
            androidContext(this@TodoApp)
        }
    }
}