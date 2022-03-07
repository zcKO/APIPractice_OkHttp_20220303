package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jc.apipractice_okhttp_20220303.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {
    }

    override fun setValues() {

        // 화면의 TextView 에 닉네임을 보여주기 위한 작업

    }

}