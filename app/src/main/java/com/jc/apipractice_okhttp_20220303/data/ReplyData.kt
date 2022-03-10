package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject
import java.text.SimpleDateFormat
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

    // 각각의 댓글이 자신의 작성 일시를, 핸드폰 시간대에 맞게 보정 + 가공된 문구로 내보내기
    fun getFormattedCreatedAt(): String {

        // 가공 해줄 양식 지정
        val sdf = SimpleDateFormat("M월 d일 a h시 m분")

        // 시차 보정에 사용할 Calendar 변수 (원래 있는 createAt 변수는 놔두고, 별도로 추가)
        // 내 폰 시간대에 맞게 보정 예정
        val localCal = Calendar.getInstance()
        // 작성 일시의 일시 값을 그대로 복사 (원래 값 : 현재 일시)
        localCal.time = this.createdAt.time

        return sdf.format(localCal.time)

    }

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
//            replyData.createdAt.set(2022, Calendar.JANUARY, 12, 10, 55, 35)
            // 임시 2) 연도만 2021 년으로 변경 (항목을 찍어서 변경)
//            replyData.createdAt.set(Calendar.YEAR, 2021)

            // 실제) 서버가 주는 created_at 에 담긴 String 을 => parse 해서, Calendar 로 변경
            // createAt 변수의 "일/시" 값으로 parse 결과물 사용.

            // 서버가 주는 양식을 보고, 그대로 찍는다.
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            // create_at 으로 내려오는 문구 (서버가 주는 String 임시 저장)
            val createdAtStr = jsonObj.getString("created_at")

            // createdAtStr 변수를 Date 로 변경 (parse) 의 결과물을 Calendar 의 time 에 대입한다.
            replyData.createdAt.time = sdf.parse(createdAtStr)


            return replyData

        }

    }


}