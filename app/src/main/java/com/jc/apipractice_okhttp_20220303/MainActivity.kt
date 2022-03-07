package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.jc.apipractice_okhttp_20220303.data.TopicData
import com.jc.apipractice_okhttp_20220303.databinding.ActivityMainBinding
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    // 실제로 서버가 내려주는 주제 목록을 담을 그릇
    val mTopicList = ArrayList<TopicData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {
    }

    override fun setValues() {

        // 메인 화면 정보 가져오기 => API 호출 / 응답 처리
        getTopicListFromServer()

    }

    private fun getTopicListFromServer() {

        ServerUtil.getRequestMainInfo(mContext, object: ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObject: JSONObject) {

                // 서버가 주는 토론 주제 목록 파싱 -> mTopicList 에 추가하기.
                val dataObj = jsonObject.getJSONObject("data")
                val topicsArr = dataObj.getJSONArray("topics")

                // topicsArr 내부를 하나씩 추출 (JOSONObject {  }) => TopicData() 로 변환.
                // JSONObject 는 for-each 를 지원하지 않는다. (차후 : ArrayList 로 변환 후 for-each 활용 예정 - retrofit)
                // Java : for (int i = 0; i < "배열".length(); i++) 와 완전히 동일한 문법.
                for (i in 0 until topicsArr.length()) {

                    // [   ] -> { }, { }, { }, ..... 순서에 맞는 {} 를 변수에 담는다.
                    // JSON 파싱의 { } => (JSONArray 에게서) JSONObject 로 추출
                    val topicObj = topicsArr.getJSONObject(i)
                    Log.d("받아낸 주제", topicObj.toString())
                }

            }

        })

    }

}