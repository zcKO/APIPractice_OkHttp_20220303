package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject

class ReplyData(
    var id: Int,
    var content: String
) {

    var writer = UserData()         // 모든 댓글에는 작성자가 있다. null 가능성이 없다.

    var selectedSide = SideData()   // 모든 댓글에는 어느 진영인지 선택한 진영이 있다. null 가능성이 없다.

    // 보조 생성자 추가 연습 : 파라미터 없는 생성자.
    constructor() : this(0, "내용 없음")

    companion object {

        fun getReplyDataFromJson(jsonObj: JSONObject): ReplyData {

            val replyData = ReplyData()

            // JSON 정보 > 멤버 변수 채우기
            replyData.id = jsonObj.getInt("id")
            replyData.content = jsonObj.getString("content")
            replyData.writer = UserData.getUserDataFromJson(jsonObj.getJSONObject("user"))

            replyData.selectedSide = SideData.getSideDataFromJson(jsonObj.getJSONObject("selected_side"))

            return replyData

        }

    }


}