package com.zlcdg.testmoshi

import com.zlcdg.libandroid.BaseDataBindingActivity
import com.zlcdg.testmoshi.databinding.ActivityTestMoshiBinding

class TestMoshiActivity : BaseDataBindingActivity<ActivityTestMoshiBinding>() {
    override fun initContentView(): Int {
        return R.layout.activity_test_moshi
    }

    override fun initView(binding: ActivityTestMoshiBinding) {
    }

    override fun initData(binding: ActivityTestMoshiBinding) {
    }
}
