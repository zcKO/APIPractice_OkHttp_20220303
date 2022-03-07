package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
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
                val topics = dataObj.getJSONArray("topics")

                // topicsArr 내부를 하나씩 추출 (JOSONObject {  }) => TopicData() 로 변환.


            }

        })

    }

}