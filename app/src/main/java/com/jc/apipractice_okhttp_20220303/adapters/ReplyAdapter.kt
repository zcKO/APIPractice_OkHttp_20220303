package com.jc.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.jc.apipractice_okhttp_20220303.R
import com.jc.apipractice_okhttp_20220303.ViewTopicDetailActivity
import com.jc.apipractice_okhttp_20220303.data.ReplyData
import com.jc.apipractice_okhttp_20220303.utils.ServerUtil
import org.json.JSONObject

class ReplyAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = LayoutInflater.from(mContext).inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val txtSelectorSide = row.findViewById<TextView>(R.id.txtSelectedSide)
        val txtWriterNickname = row.findViewById<TextView>(R.id.txtWriterNickname)
        val txtReplyContent = row.findViewById<TextView>(R.id.txtReplyContent)
        val txtCreatedAt = row.findViewById<TextView>(R.id.txtCreatedAT)

        txtReplyContent.text =  data.content
        txtWriterNickname.text = data.writer.nickname
        txtSelectorSide.text = "[${data.selectedSide.title}]"

        val txtReReplyCount = row.findViewById<TextView>(R.id.txtReReplyCount)
        val txtLikeCount = row.findViewById<TextView>(R.id.txtLikeCount)
        val txtHateCount = row.findViewById<TextView>(R.id.txtHateCount)

        // 임시 1 - 작성 일자만 "2022-03-10" 형태로 표현 => "연 / 월 / 일" 데이터로 가공
        // 월은 1 작게 나온다. +1 로 보정
//        txtCreatedAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH) + 1}-${data.createdAt.get(Calendar.DAY_OF_MONTH)}"

        // 실제 활용
        // 임시 2 - "2022-03-10" 형태로 표현 => SimpleDateFormat 활용
//        val sdf = SimpleDateFormat("yyyy-MM-dd")

        // SimpleDateFormat (Date 객체) => 지정해둔 양식의 String 으로 가공.
        // createAt 은 Calendar / format 의 파라미터 : Data => Calendar 의 내용물인 time 변수가 Date 타입
//        txtCreatedAt.text = sdf.format(data.createdAt.time)

        // 연습
        // 양식 1) 2022년 3월 5일
        // 양식 2) 220305
        // 양식 3) 3월 5일 오전 2시 5분
        // 양식 4) 21년 3/5 (토) - 18:05
//        val sdf1 = SimpleDateFormat("yyyy년 M월 d일")
//        val sdf2 = SimpleDateFormat("yyyyMMdd")
//        val sdf3 = SimpleDateFormat("M월 d일 a h시 m분")
//        val sdf4 = SimpleDateFormat("yy년 m/d (E) - HH:mm")

        txtCreatedAt.text = data.getFormattedCreatedAt()

        txtReReplyCount.text = "답글 ${data.reReplyCount}"
        txtLikeCount.text = "좋아요 ${data.likeCount}"
        txtHateCount.text = "싫어요 ${data.hateCount}"

        txtLikeCount.setOnClickListener {

            // 서버에 이 댓글에 좋아요 알림.
            ServerUtil.postRequestReplyLikeOrHate(
                mContext,
                data.id,
                true,
                object: ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObject: JSONObject) {

                        // 무조건 댓글 목록 새로 고침
                        // Adapter 에서 코딩 -> 실행하고 싶은 것은 액티비티의 기능 실행

                        // 어댑터 객체화시, mContext 변수에 어느 화면에서 사용하는지 대입하고 있다.
                        // mContext : Context 타입. 대입 하는 객체 : ViewTopicActivity 의 객체가 들어있다. => 다형성

                        // 부모 형태의 변수에 담긴 자식 객체는, 캐스팅을 통해서 원상 복구 가능
                        // 자식에서 만든 별도의 함수들을 다시 사용 가능.

//                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()

                        if (mContext is ViewTopicDetailActivity) {
                            mContext.getTopicDetailFromServer()
                        }

                    }

                }
            )

        }

        // [도전 과제] 싫어요가 눌려도 마찬가기 처리. => 싫어요 API 호출 (기존 함수 활용) + 토론 상세화면 댓글 목록 새로고침
        txtHateCount.setOnClickListener {

            // 서버에 이 댓글에 좋아요 알림.
            ServerUtil.postRequestReplyLikeOrHate(
                mContext,
                data.id,
                false,
                object: ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObject: JSONObject) {

                        // 무조건 댓글 목록 새로 고침
                        // Adapter 에서 코딩 -> 실행하고 싶은 것은 액티비티의 기능 실행

                        // 어댑터 객체화시, mContext 변수에 어느 화면에서 사용하는지 대입하고 있다.
                        // mContext : Context 타입. 대입 하는 객체 : ViewTopicActivity 의 객체가 들어있다. => 다형성

                        // 부모 형태의 변수에 담긴 자식 객체는, 캐스팅을 통해서 원상 복구 가능
                        // 자식에서 만든 별도의 함수들을 다시 사용 가능.

//                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()

                        if (mContext is ViewTopicDetailActivity) {
                            mContext.getTopicDetailFromServer()
                        }

                    }

                }
            )

        }

        // 좋아요가 눌렸는지, 아닌지. 글씨 색상 변경 / 배경 drawable 도 설정
        if (data.isMyLike) {
            txtLikeCount.setTextColor(ContextCompat.getColor(mContext, R.color.naver_red))
            txtLikeCount.setBackgroundResource(R.drawable.naver_red_border)
        } else {
            txtHateCount.setTextColor(ContextCompat.getColor(mContext, R.color.deep_dark_gray))
            txtHateCount.setBackgroundResource(R.drawable.dark_gray_border_box)

        }

        return row

    }

}