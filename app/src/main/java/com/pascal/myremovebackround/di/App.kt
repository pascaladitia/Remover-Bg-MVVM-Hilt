package com.pascal.myremovebackround.di

import android.app.Application
import android.support.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        MultiDex.install(this)
    }
}