package com.jc.apipractice_okhttp_20220303

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.jc.apipractice_okhttp_20220303.databinding.ActivityLoginBinding
import com.jc.apipractice_okhttp_20220303.utils.ContextUtil
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.btnSignUp.setOnClickListener {

            // 단순 화면 이동.
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)

        }


        binding.btnLogin.setOnClickListener {

            val inputId = binding.edtId.text.toString()
            val inputPw = binding.edtPassword.text.toString()

            // API 서버에 아이디 / 비번을 보내서 실제로 회원인지 검사 => 로그인 시도.
            ServerUtil.postRequestLogin(inputId, inputPw, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObject: JSONObject) {

                    // 화면의 입장에서, 로그인 결과를 받아서 처리할 코드
                    // 서버에 다녀오고 실행 : 라이브러리가 자동으로 백그라운드에서 돌도록 만들어 둔 코드
                    val code = jsonObject.getInt("code")

                    if (code == 200) {

                        // 로그인 한 사람의 닉네임을 추출, "***님, 환영합니다." 로 토스트 출력
                        val dataObj = jsonObject.getJSONObject("data")
                        val userObj = dataObj.getJSONObject("user")
                        val nickname = userObj.getString("nick_name")

                        runOnUiThread {
                            Toast.makeText(mContext, "${nickname}님, 환영합니다!", Toast.LENGTH_SHORT).show()
                        }

                        // 서버가 내려준 토근 값을 변수에 담아보자.
                        val token = dataObj.getString("token")
                        ContextUtil.setToken(mContext, token)


                        // 변수에 담긴 토큰 값(String) 을 SharedPreferences 에 담아주자.
                        // 로그인 성공시에는 담기만, 필요한 화면 / 클래스에서 꺼내서 사용.


                        // 메인화면 진입 => 클래스의 객체화 (UI 동작으로 하지 않는다.)
                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)

                    } else {

                        val message = jsonObject.getString("message")

                        // Toast : UI 조작 하는 코드 => 백그라운드에서 UI 를 건드리면, 위험한 동작으로 간주하고 앱을 강제 종료한다.
                        runOnUiThread {
                            // Toast 를 띄우는 코드만, UI 전담 스레드에서 실행하도록 한다.
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }

                }

            })

        }
    }

    override fun setValues() {

        // 이전에 설정한 자동 로그인 여부를 미리 체크해두자.
        // 종료를 해도 계속 반영 => 반 영구적으로 저장. => SharedPPreferences 에서 관리

        // 저장되어 있는 자동 로그인 여부 값을 체크 박스에 반영
        binding.autoLoginCheckBox.isChecked = ContextUtil.getAutoLogin(mContext)

    }


}