package com.zlcdg.libandroid

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingActivity<VDB : ViewDataBinding> : BaseActivity() {

    lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = initContentView()
        setContentView(layoutId)
        binding = DataBindingUtil.setContentView(this, layoutId)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}