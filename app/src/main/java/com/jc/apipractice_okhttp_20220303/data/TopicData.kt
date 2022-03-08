package com.jc.apipractice_okhttp_20220303.data

import java.io.Serializable

class TopicData : Serializable {

    var id = 0              // id 는 Int 라고 명시
    var title = ""          // title 은 String 이라고 명시
    var imageURL = ""       // 서버 : img_url, 앱 : imageURL 변수명이 다른 경우
    var replyCount = 0

}