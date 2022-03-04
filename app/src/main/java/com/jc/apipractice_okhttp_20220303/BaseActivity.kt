package com.jc.apipractice_okhttp_20220303

import androidx.appcompat.app.AppCompatActivity

// 다른 모든 화면이 공통적으로 ㅏ질 기능 / 멤버 변수를 모아두는 (부모) 클래스

abstract class BaseActivity : AppCompatActivity(){

    // setupEvents / setValues 함수를 만들어 물려준다.
    // 실제 함수를 구현해서 돌려 줘봐야, 오버라이딩 해서 사용한다.
    //   => 추상 메서드로 돌려줘서, 반드시 오버라이딩 하게 만든다.
    abstract fun setupEvents()
    abstract fun setValues()

}