package com.jc.apipractice_okhttp_20220303.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

    // ServerUtil 로 돌아온 응답을 Activity 에서 처리하도록 일처리 넘기기
    // 나에게 생긴일을 다른 클래스에게 처리 요청 - interface 활용
    interface JsonResponseHandler {
        fun onResponse(jsonObject: JSONObject)
    }

    // 서버에 Request 를 보내는 역할
    // 함수를 만들 때, 어떤 객체가 실행해도 결과만 잘 나오면 그만인 함수.
    // Kotlin 에서 static 에 해당하는 개념 -> companion object {  }

    companion object {

        // 서버 컴퓨터 주소만 변수로 저장 (관리 일원화) -> 외부 노출은 안되게 한다.
        private const val BASE_URL = "http://54.180.52.26"

        // 로그인 기능 호출 함수
        // handler : 이 함수를 사용하는 화면에서, JSON 분석을 어떻게 UI 에서 활용할지의 방안 (인터페이스)
        //  - 처리 방안을 임시로 비워두려면, Null 대입 허용.
        fun postRequestLogin(id: String, pw: String, handler: JsonResponseHandler?) {
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
                    // 실패 : 서버 연결 자체를 실패. 응답이 오지 않는 경우.
                    // ex. 인터넷 끊김, 서버 접속 불가 등등 물리적 연결 실패
                    // ex. 비번이 틀려서 로그인 실패 : 서버 연결 성공, 응답도 돌아오는 상황 -> 그 내용만 실패. (물리적 실패가 아니다.)
                    // 그렇기에 여기서 단순 로그인과 비밀번호 실패는 다루지 않는다.
                }

                override fun onResponse(call: Call, response: Response) {

                    // 어떤 내용이던, 응답 자체는 잘 돌아온 경우. (그 내용이 성공 / 실패 일 수 있다.)
                    // 응답 : response 변수 > 응답의 본문 (body) 만 보자
                    // toString() 이 아니다. string() 은 한번만 사용 즉, 1회용이며, 변수에 담아두고 이용
                    // 다음 줄에서 다시 .string() 을 사용하면 에러
                    val bodyString = response.body!!.string()

                    // 응답의 분문을 String 으로, 변환하면, JSON Encoding 이 적용된 상태이다. (한글 깨짐)
                    // JSONObject 객체로 응답 본문 String 을 변환해주면, 한글이 복구 된다.
                    //   => UI 에서도 JSONObject 를 이용해서, 데이터 추출 / 실제 활용.
                    val jsonObj = JSONObject(bodyString)


                    Log.d("서버 테스트", jsonObj.toString())

                    // 실제 : handler 변수에, jsonObj 를 가지고 화면에서 어떻게 처리할지 계획이 들어와있다.
                    // (계획이 되어있을 때만) 해당 계획을 실행하자.
                    handler?.onResponse(jsonObj)


                }
            })

        }

        // 회원가입 기능 호출 함수
        fun putRequestSignUp(
            id: String,
            pw: String,
            nickname: String,
            handler: JsonResponseHandler?
        ) {

            val urlString = "${BASE_URL}/user"

            val formData = FormBody.Builder()
                .add("email", id)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .put(formData)
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObject = JSONObject(bodyString)
                    Log.d("서버 응답", jsonObject.toString())
                    handler?.onResponse(jsonObject)

                }
            })

        }

        // 이메일, 닉네임 중복 검사 함수
        fun getRequestDuplicatedCheck(
            type: String,
            inputValue: String,
            handler: JsonResponseHandler?
        ) {

            // 1) 어느 주소로 가야하는가 + 어떤 파라미터를 첨부하는가 도 주소에 같이 포함
            //   => 라이브러리의 도움을 받자. HttpUrl 클래스 (OkHttp 소속)
            val urlBuilder = "${BASE_URL}/user_check"
                .toHttpUrlOrNull()!!
                .newBuilder()
                .addEncodedQueryParameter("type", type)         // 크롬이나 브라우저에 들어갈 쿼리이기에 Encoded 를 안붙이면 한글이 깨진다.
                .addEncodedQueryParameter("value", inputValue)  // encodedName 은 서버에 요청하는 파라미터
                .build()

            val urlString = urlBuilder.toString()

            // 2) 실제 요청 정보 정리 > Request 생성
            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

            // 3) Request 완성 > 서버에 호출, 응답을 화면에 넘긴다.
            val client = OkHttpClient()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {
                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버 응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)


                }

            })

        }

        // 연습 : 내정보 불러오기 (/user_info - GET)
        // 토큰은 ContextUtil 클래스에서 getToken 함수로 가져 올 수 있다.
        // 토큰 값 자체는 파라미터로 받아 올 필요 없다 => ContextUtil 을 불러다 사용한다.
        // 메모장에 접근할 수 있게, Context 변수 하나를 미리 받아 둔다.
        fun getRequestMyInfo(context: Context, handler: JsonResponseHandler?) {

            val urlBuilder = "${BASE_URL}/user_info"
                .toHttpUrlOrNull()!!
                .newBuilder()
                .build()            // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))       // ContextUtil 을 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }

            })

        }

        fun getRequestMainInfo(context: Context, handler: JsonResponseHandler?) {

            val urlBuilder = "${BASE_URL}/v2/main_info"
                .toHttpUrlOrNull()!!
                .newBuilder()
                .build()            // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))       // ContextUtil 을 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }

            })

        }

        fun getRequestTopicDetail(context: Context,  topicId: Int, handler: JsonResponseHandler?) {

//            val urlBuilder = "${BASE_URL}/topic"
//                .toHttpUrlOrNull()!!
//                .newBuilder()
//                .addPathSegment(topicId.toString())
//                .build()            // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리

            val urlBuilder = "${BASE_URL}/topic/${topicId}"
                .toHttpUrlOrNull()!!
                .newBuilder()
                .build()            // 쿼리 파라미터를 담을게 없다. 바로 build 로 마무리

            val urlString = urlBuilder.toString()

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))       // ContextUtil 을 통해, 저장된 토큰을 받아서 첨부
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {

                    val jsonObj = JSONObject(response.body!!.string())
                    Log.d("서버응답", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }

            })

        }

        fun postRequestVote(context: Context, sideId: Int, handler: JsonResponseHandler?) {

            val urlString = "${BASE_URL}/topic_vote"

            val formData = FormBody.Builder()
                .add("side_id", sideId.toString())
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    call.cancel()
                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()
                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버 테스트", jsonObj.toString())
                    handler?.onResponse(jsonObj)

                }
            })

        }

    }

}