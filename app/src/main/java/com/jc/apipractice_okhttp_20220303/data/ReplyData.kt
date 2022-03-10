package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject
import java.util.*

class ReplyData(
    var id: Int,
    var content: String
) {

    var writer = UserData()         // 모든 댓글에는 작성자가 있다. null 가능성이 없다.

    var selectedSide = SideData()   // 모든 댓글에는 어느 진영인지 선택한 진영이 있다. null 가능성이 없다.

    // 작성 일시를 담아둘 변수
    // "일 / 시" 데이터를 변경 => 내부의 숫자만 변경, 변수에 새 객체 대입이 아니라서 val 을 사용해도 된다.
    val createdAt = Calendar.getInstance()

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

            // Calendar 로 되어있는 작성일시의 시간을, 서버가 알려주는 댓글 작성 일시로 맞춰줘야 한다.

            // 임시 1) 2022-01-12 10:55:35 로 변경 (한번에 모두 변경)
            replyData.createdAt.set(2022, 1, 12, 10, 55, 35)

            // 임시 2) 연도만 2021 년으로 변경 (항목을 찍어서 변경)
            replyData.createdAt.set(Calendar.YEAR, 2021)

            return replyData

        }

    }


}