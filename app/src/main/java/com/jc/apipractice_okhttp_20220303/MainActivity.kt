package com.jc.apipractice_okhttp_20220303

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.jc.apipractice_okhttp_20220303.adapters.TopicAdapter
import com.jc.apipractice_okhttp_20220303.data.TopicData
import com.jc.apipractice_okhttp_20220303.databinding.ActivityMainBinding
import com.jc.apipractice_okhttp_20220303.utils.ContextUtil
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    // 실제로 서버가 내려주는 주제 목록을 담을 그릇
    val mTopicList = ArrayList<TopicData>()

    lateinit var mAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        binding.topicListview.setOnItemClickListener { parent, view, position, id ->

            val clickedTopic = mTopicList[position]

            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topic", clickedTopic)
            startActivity(myIntent)

        }

        binding.btnLogout.setOnClickListener {

            // 경고창 > 확인시 로그아웃
            val alert = AlertDialog.Builder(mContext)
                .setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

                    // 로그인 : 토큰 값을 받아서 기기에 저장하는 행위
                    // 로그아웃 : 토큰 값을 삭제 처리하는 행위 (폰에서 삭제 > 서버에서 받아올 필요가 없다.)
                    ContextUtil.setToken(mContext, "")

                    val myIntent = Intent(mContext, LoginActivity::class.java)
                    startActivity(myIntent)
                    finish()

                })
                .setNegativeButton("취소", null)
                .show()

        }

    }

    override fun setValues() {

        // 액션바의 백버튼 (BaseActivity 가 물려줌) 숨김 처리
        btnBack.visibility = View.GONE

        // 메인 화면 정보 가져오기 => API 호출 / 응답 처리
        // 코드 상으로는 먼저 실행시키지만, 비동기로 움직이기에, 완료는 어댑터 연결보다 늦을 수 도 있다.
        //  => 목록에 토론 주제 추가 : 어댑터 연결 이후 추가
        // ListView Adapter 의 내용물들에 변경 : notifyDataSetChanged 실행
        getTopicListFromServer()


        mAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        binding.topicListview.adapter = mAdapter

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

                    // TopicData 변수 생성 => 멤버 변수에, topicObj 가 들고 있는 값들을 대입
                    val topicData = TopicData.getTopicDataFromJson(topicObj)

                    // 완성된 TopicData 객체를 목록에 추가
                    mTopicList.add(topicData)

                }

                // ListView 의 내용물을 새로 고침 => UI 에 내용물 변경 행위
                runOnUiThread {
                    mAdapter.notifyDataSetChanged()
                }

            }

        })

    }

}