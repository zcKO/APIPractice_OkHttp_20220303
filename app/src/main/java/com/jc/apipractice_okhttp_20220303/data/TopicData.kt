package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject
import java.io.Serializable

class TopicData : Serializable {

    var id = 0              // id 는 Int 라고 명시
    var title = ""          // title 은 String 이라고 명시
    var imageURL = ""       // 서버 : img_url, 앱 : imageURL 변수명이 다른 경우
    var replyCount = 0

    // 하나의 토론주제 : 여러 개의 (목록) 선택 진영이 필요하다.
    val sideList = ArrayList<SideData>()

    companion object {
        // 토론 주제 정보를 담고 있는 JSONObject 가 들어오면 > TopicData 형태로 변환해주는 함수. => static 메서드
        fun getTopicDataFromJson(jsonObj: JSONObject): TopicData {

            // 기본 내용의 TopicData 생성
            // jsonObj 에서 데이터 추출 > 멤버 변수 대입
            val topicData = TopicData().apply {
                id = jsonObj.getInt("id")
                title = jsonObj.getString("title")
                imageURL = jsonObj.getString("img_url")
                replyCount = jsonObj.getInt("reply_count")
            }

            // sides 라는 JSONArray 가 들어 있다.
            //   => topicData 의 하위 정보로, 선택진영 목록으로 저장.
            //   => JSONArray > ArrayList

            val sidesArr = jsonObj.getJSONArray("sides")

            for (i in 0 until sidesArr.length()) {

                // 선택 진영 정보를 들고 있는 JSONObject 추출
                val sideObj = sidesArr.getJSONObject(i)

                // sideObj 도, SideData 로 (선택 진영)
                val sideData = SideData.getSideDataFromJson(jsonObj)

                // topicData 변수의 하위 목록으로 등록
                topicData.sideList.add(sideData)
            }


            // 완성된 TopicData 리턴
            return topicData

        }


    }


}