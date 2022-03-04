package com.jc.apipractice_okhttp_20220303.utils

import okhttp3.*
import java.io.IOException

class ServerUtil {

    // 서버에 Request 를 보내는 역할
    // 함수를 만들 때, 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수.
    // Kotlin 에서 static 에 해당하는 개념 -> companion object {  }

    companion object {

        // 서버 컴퓨터 주소만 변수로 저장 (관리 일원화) -> 외부 노출은 안되게 한다.
        private const val BASE_URL = "http://54.180.52.26"

        // 로그인 기능 호출 함수
        fun postRequestLogin(id: String, pw: String) {
            // request 제작 -> 실제 호출 -> 서버의 응답을 화면에 전달

            // 제작 1) 어느 주소 (url) 로 접근할지 지정 -> 서버 주소 + 기능 주소
            val urlString = "${BASE_URL}/user"

            // 제작 2) 파라미터 담아주기 => 어떤 이름표를 어느 공간에
            val formData = FormBody.Builder()
                .add("email", id)
                .add("password", pw)
                .build()

            // 제작 3) 모든 Request 정보를 종합한 객체 생성. (어느 주소로 + 어느 메서드로 + 어떤 파라미터를)
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

            // 종합한 Request 도 실제 호출을 해 주어야 API 호출이 실행된다. (startActivity 와 같은 동작이라고 생각하면 된다.)
            // 실제 호출 : 앱이 클라이언트로서 동작 > OkHttpClient 클래스
            val client = OkHttpClient()

            // request 의 정보를 서버로 보낸다.
            // OkHttpClient 객체를 이용 -> 서버에 로그인 기능 실제 호출
            // 호출을 했으면, 서버가 수행한 결괄르 받아서 처리
            //   => 서버에 다녀와서 할 일을 등록 : enqueue(Callback)
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                }
            })

        }

    }

}