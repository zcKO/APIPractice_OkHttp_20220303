package com.jc.apipractice_okhttp_20220303.data

import android.util.Log
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

        // localCal 의 값을, 내 폰 설정에 맞게 시간을 더하자.
        // ex) 한국 : +9 를 더해야한다.
        // ex) LA : -8 을 더해야한다.

        // Calendar 객체의 기능으로, 시간대 정보를 추출 기능이 있다.
        val localTimeZone = localCal.timeZone
        Log.d("TAG", "time zone = $localTimeZone")

        // 시간 대가, GMT 와의 시차가 얼마나 나는지, (rawOffset - 몇 ms 차이까지)
        // 초를 분으로 구하려면 60 으로 나누고, 시를 구하려면 다시 60 으로 나눈다.
        val diffHour = localTimeZone.rawOffset / 60 / 60 / 1000         //  ms => 시간으로 변경
        Log.d("TAG", "diffHour = $diffHour")

        // 구해진 시차를, 기존 시간에서 시간 값으로 더해주기
        localCal.add(Calendar.HOUR, diffHour)
        Log.d("TAG", "localCal = $localCal.time")

        // 현재 시간과의 차이를 구해서, 차이 값에 따라 다른 양식으로 가공.
        // ex) 2분 전 작성 => "2분 전"
        // ex) 5일 전 작성 => "3월 10일 오전 5시 6분"


        val now = Calendar.getInstance()          // 현재 시간 표현 변수 (핸드폰 신가대가 적용되어 있다.)
//        now.add(Calendar.HOUR, diffHour)        // 현재 시간도 별도 변수로 생성 + 시차 보정

        // 현재 시간 - 작성일시 ("몇 시간 / 몇 분")
        // timeInMillis 의 기준점 1970-01-01
        // 1970.1.1 ~ 1970.1.2
        // 1 * 24 * 60 * 60 * 1000  / 일, 시, 분, 초, ms
        val timeAgo =
            now.timeInMillis - localCal.timeInMillis  // 현재 시간 - 작성일시 결과. (몇 ms 차이나는지 구할 수 있다.)

        // 5초 이내 => "방금 전"
        if (timeAgo < 5 * 1000) {
            return "방금 전"
        } else if (timeAgo < 60 * 1000) {
            // 1분 이내 => "?초 전"
            // timeAgo : ms 단위로 계산됨 => ? 조건을 보여주려면, 초 단위로 변환
            val diffSecond = timeAgo / 1000
            return "${diffSecond}초 전"
        } else if (timeAgo < 1 * 60 * 60 * 1000) {
            // 1 시간 이내에 작성된 글? => ? 분 전
            val diffMinute = timeAgo / 1000 / 60
            return "${diffMinute}분 전"
        } else if (timeAgo < 24 * 60 * 60 * 1000) {
            // 24 시간 이내 : ?시간 전
            val diff = timeAgo / 1000 / 60 / 60
            return "${diff}시간 전"
        } else if(timeAgo < (10*24) * 60 * 60 * 1000) {
            // 10일 이내 : ?일 전
            val diffDay = timeAgo / 1000 / 60 / 60 / 24
            return "${diffDay}일 전"
        } else {
            // 10일 이상 : sdf 가공해서 리턴
            return sdf.format(localCal.time)
        }

    }

    companion object {

        fun getReplyDataFromJson(jsonObj: JSONObject): ReplyData {

            val replyData = ReplyData()

            // JSON 정보 > 멤버 변수 채우기
            replyData.id = jsonObj.getInt("id")
            replyData.content = jsonObj.getString("content")
            replyData.writer = UserData.getUserDataFromJson(jsonObj.getJSONObject("user"))

            replyData.selectedSide =
                SideData.getSideDataFromJson(jsonObj.getJSONObject("selected_side"))

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