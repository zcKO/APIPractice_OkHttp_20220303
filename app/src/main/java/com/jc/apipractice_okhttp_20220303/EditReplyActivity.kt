package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jc.apipractice_okhttp_20220303.databinding.ActivityEditReplyBinding

class EditReplyActivity : BaseActivity() {

    lateinit var binding: ActivityEditReplyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_reply)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}