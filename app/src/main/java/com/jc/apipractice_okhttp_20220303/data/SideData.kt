package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject
import java.io.Serializable

// 토론의 선택 진영 (이름, id 값 등등)을 표현
class SideData: Serializable {
    var id = 0
    var title = ""
    var voteCount = 0

    companion object {

        fun getSideDataFromJson(jsonObj: JSONObject) : SideData {

            return SideData().apply {
                id = jsonObj.getInt("id")
                title = jsonObj.getString("title")
                voteCount = jsonObj.getInt("vote_count")
            }

        }

    }

}