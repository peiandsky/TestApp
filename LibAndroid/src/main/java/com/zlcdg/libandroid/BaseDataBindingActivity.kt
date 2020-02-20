package com.zlcdg.libandroid

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseDataBindingActivity<VDB : ViewDataBinding> : BaseActivity() {

    private lateinit var binding: VDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutId = initContentView()
        setContentView(layoutId)
        binding = DataBindingUtil.setContentView(this, layoutId)

        initView(binding)
        initData(binding)
    }

    /**
     * 初始化界面布局等
     */
    abstract fun initView(binding: VDB)

    /**
     * 初始化数据
     */
    abstract fun initData(binding: VDB)


    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}