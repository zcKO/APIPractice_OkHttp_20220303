package com.jc.apipractice_okhttp_20220303

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jc.apipractice_okhttp_20220303.adapters.ReplyAdapter
import com.jc.apipractice_okhttp_20220303.data.ReplyData
import com.jc.apipractice_okhttp_20220303.data.TopicData
import com.jc.apipractice_okhttp_20220303.databinding.ActivityViewTopicDetailBinding
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var binding: ActivityViewTopicDetailBinding

    // 보여주게 될 토론 주제 데이터 > 이벤트 처리, 데이터 표현 등 여러 함수에서 사용
    lateinit var mTopicData: TopicData

    val mReplyList = ArrayList<ReplyData>()

    lateinit var mAdapter: ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_topic_detail)
        mTopicData = intent.getSerializableExtra("topic") as TopicData

        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        // btnVote1 클릭 => 첫 진영의 id 값을 찾아서, 거기에 투표
        // 서버 전달 => API 활용
        binding.btnVote1.setOnClickListener {

            // 서버 투표 API 호출
            ServerUtil.postRequestVote(mContext, mTopicData.sideList[0].id, object: ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObject: JSONObject) {

                    // 토스트로, 서버가 알려주는 현재 상황 (신규 투표 or 재투표 or 취소 등)
                    val message = jsonObject.getString("message")

                    runOnUiThread {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    // 변경된 득표 현황을 다시 불러오자.
                    getTopicDetailFromServer()
                }

            })

            // 투표 현황을 새로 고침 (응답)

        }

        // 2번 진영 선택시, 그 진영에 투표하기 (연습문제)
        binding.btnVote2.setOnClickListener {

            ServerUtil.postRequestVote(mContext, mTopicData.sideList[1].id, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(jsonObject: JSONObject) {

                    val message = jsonObject.getString("message")

                    runOnUiThread {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }

                    getTopicDetailFromServer()

                }


            })

        }

    }

    override fun setValues() {

        mAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        binding.replyListView.adapter = mAdapter

        setTopicDataToUi()

        getTopicDetailFromServer()

    }

    fun setTopicDataToUi() {
        // 토론 주제에 대한 데이터들을, UI 에 반영하는 함수
        // 화면 초기 진입 실행 + 서버에서 다시 받아왓을 때도 실행
        binding.txtTitle.text = mTopicData.title
        Glide.with(mContext)
            .load(mTopicData.imageURL)
            .into(binding.imgTopicBackground)

        // 1번 진영 제목, 2번 진영 제목
        binding.txtSide1.text = mTopicData.sideList[0].title
        binding.txtSide2.text = mTopicData.sideList[1].title

        // 1번 진영 득표수 ,2번 진영 득표수
        binding.txtVoteCount1.text = "${mTopicData.sideList[0].voteCount} 표"
        binding.txtVoteCount2.text = "${mTopicData.sideList[1].voteCount} 표"

        // 내가 선택한 진형이 있을 때 (투표 해놨을 때)
        // 이미 투표한 진영은 문구를 변경하자 ("투표 취소하기")
        if (mTopicData.mySelectedSide != null) {
            // 첫 번째 진영을 투표했는지
            // 두 번째 진영을 투표했는지
            if (mTopicData.mySelectedSide!!.id == mTopicData.sideList[0].id) {
                // 첫 번째 진영에 투표했을 경우
                binding.btnVote1.text = "투표 취소"
                binding.btnVote2.text = "다시 투표"
            } else {
                // 두 번째 진영에 투표했을 경우
                binding.btnVote1.text = "다시 투표"
                binding.btnVote2.text = "투표 취소"
            }

        } else {
            // 아무 곳에도 투표하지 않는 경우
            binding.btnVote1.text = "투표 하기"
            binding.btnVote2.text = "투표 하기"
        }

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

                runOnUiThread {
                    setTopicDataToUi()
                }

                // topicObj 내부에는 replies 라는 댓글 목록 JSONArray 도 들어있다.
                // mReplyList 에 넣어준다.
                val repliesArr = topicObj.getJSONArray("replies")

                for (i in 0 until repliesArr.length()) {
                    val replyObj = repliesArr.getJSONObject(i)

                    mReplyList.add(ReplyData.getReplyDataFromJson(replyObj))
                }

                // 경우에 따라 서버의 동작이므로, 어댑터 세팅보다 늦게 끝날 수 있다. (notifyDataSetChanged)
                runOnUiThread {
                    mAdapter.notifyDataSetChanged()
                }
            }

        })
    }

}