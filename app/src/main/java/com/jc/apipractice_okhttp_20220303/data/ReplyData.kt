package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject

class ReplyData(
    var id: Int,
    var content: String
) {

    // 보조 생성자 추가 연습 : 파라미터 없는 생성자.
    constructor() : this(0, "내용 없음")

    companion object {

        fun getReplyDataFromJson(jsonObj: JSONObject): ReplyData {

            val replyData = ReplyData()

            // JSON 정보 > 멤버 변수 채우기

            return replyData

        }

    }


}