package com.jc.apipractice_okhttp_20220303.utils

import android.content.Context
import android.content.SharedPreferences

class ContextUtil {

    companion object {

        // 메모장 파일 이름처럼, SharedPreferences 의 이름 설정
        private val prefName = "OkHttpPracticePref"

        // 저장할 데이터의 항목명도 변수로 만들어두자.
        val TOKEN = "TOKEN"

        // 데이터를 저장하는 함수 (setter) / 조회하는 함수 (getter) 별개로 작성해야한다.
        // TOKEN 항목에 저장 => token 항목 조회시 데이터 인식 x 대소문자까지 동일해야한다.
        // 오판을 줄이고, 코딩을 편하게 하는 조치로 TOKEN 변수 생성.

        fun setToken(context: Context, token: String) {
            // 메모장 파일을 열어야 무엇을 작성할 수 있다.
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)  // MODE_PRIVATE 우리 앱에서만 사용.
            // 입력 들어온 token 내용에 (TOKEN 항목에) 저장.
            pref.edit().putString(TOKEN, token).apply()
        }

    }

}