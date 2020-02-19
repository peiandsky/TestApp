package com.zlcdg.libandroid

import android.app.Application
import androidx.multidex.MultiDex

open class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}