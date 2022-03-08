package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

        // 2.5 초가 지나면 -> 자동로그인을 해도 되는지 판단 -> 상황에 맞는 화면으로 이동.
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed({

            // 자동 로그인을 해도 되는지 질문
            // 1. 사용자가 자동로그인 의사를 OK 했는지 확인
            // 2. 로그인 시에 받아낸 토큰 값이 지금도 유효한지 검증
            if () {
                // 둘다 OK 라면 메인화면으로 이동

            } else {
                // 아니라면, 로그인 화면으로 이동
            }

        }, 2500)


    }

}