package com.jc.apipractice_okhttp_20220303

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

// 다른 모든 화면이 공통적으로 가질 기능 / 멤버 변수를 모아두는 (부모) 클래스

abstract class BaseActivity : AppCompatActivity() {

    // Context 계열의 파라미터에 대입할 때, 보통 this 로 대입
    // 인터페이스가 엮이기 시작한다면 this@ 어느 화면인지 추가로 고려해야 한다.

    // 미리 mContext 의 변수에, 화면의 this 를 담아두고 모든 Activity 에 상속으로 물려준다. (좋은 코드는 아니다, 임시 방편)
    lateinit var mContext: Context

    // 액션바에서 UI 변수를 멤버변수로 만들어야 상속으로 물려주는 것이 가능.
    //  => 변수에 대입 : 커스텀 액션바 세팅 뒤에 진행해야한다.
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this     // 화면이 만들어 질 때, this 대입

        supportActionBar?.let {
            // supportActionBar 변수가 null 이 아닐 때만 실행할 코드
            setCustomActionBar()
        }

    }


    // setupEvents / setValues 함수를 만들어 물려준다.
    // 실제 함수를 구현해서 돌려 줘봐야, 오버라이딩 해서 사용한다.
    //   => 추상 메서드로 돌려줘서, 반드시 오버라이딩 하게 만든다.
    abstract fun setupEvents()
    abstract fun setValues()

    // 실제 구현 내용을 같이 물려주는 함수. (일반 함수)
    // 액션바 설정 기능
    fun setCustomActionBar() {

        val defaultActionBar = supportActionBar!!
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        defaultActionBar.setCustomView(R.layout.my_custom_action_bar)

        val toolbar = defaultActionBar.customView.parent as Toolbar
        // 절대 값
        toolbar.setContentInsetsAbsolute(0, 0)

        // xml 에 그려둔 UI 가져오기
        btnBack = defaultActionBar.customView.findViewById(R.id.btnBack)

        // 누르면 화면 종료 : 모든 화면 공통
        btnBack.setOnClickListener {
            finish()
        }

    }

}