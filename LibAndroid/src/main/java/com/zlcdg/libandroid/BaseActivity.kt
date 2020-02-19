package com.zlcdg.libandroid

import android.os.Bundle

abstract class BaseActivity : BaseTempActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentView())
    }

    abstract fun initContentView(): Int

}