package com.jc.apipractice_okhttp_20220303.data

import org.json.JSONObject

class UserData {

    var id = 0
    var email = ""
    var nickname = ""

    companion object {

        fun getUserDataFromJson(jsonObj: JSONObject): UserData {

            val userData = UserData()

            userData.id = jsonObj.getInt("id")
            userData.email = jsonObj.getString("email")
            userData.nickname = jsonObj.getString("nick_name")

            return userData

        }

    }


}