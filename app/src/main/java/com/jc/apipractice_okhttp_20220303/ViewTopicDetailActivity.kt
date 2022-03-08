package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jc.apipractice_okhttp_20220303.data.TopicData
import com.jc.apipractice_okhttp_20220303.databinding.ActivityViewTopicDetailBinding
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var binding: ActivityViewTopicDetailBinding

    // 보여주게 될 토론 주제 데이터 > 이벤트 처리, 데이터 표현 등 여러 함수에서 사용
    lateinit var mTopicData: TopicData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topic_detail)
        mTopicData = intent.getSerializableExtra("topic") as TopicData

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

    }

    override fun setValues() {

        binding.txtTitle.text = mTopicData.title
        Glide.with(mContext)
            .load(mTopicData.imageURL)
            .into(binding.imgTopicBackground)

        getTopicDetailFromServer()

    }

    fun getTopicDetailFromServer() {
        // 서버 주소/topic/{1} : {} -> Path Segment
        ServerUtil.getRequestTopicDetail(mContext, mTopicData.id, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(jsonObject: JSONObject) {

                val dataObj = jsonObject.getJSONObject("data")
                val topicObj = dataObj.getJSONObject("topic")

                // 토론 정보 JSONObject 인 (topicObj) => TopicData() 형태로 변환 (여러 화면에서 진행 가능, 함수로 만든다.)
                val topicData = TopicData.getTopicDataFromJson(topicObj)

                // 변환된 객체를, mTopicData 를 다시 대입 => UI 반영도 다시 실행
                mTopicData = topicData

            }

        })
    }

}