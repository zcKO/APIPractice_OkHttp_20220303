package com.jc.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jc.apipractice_okhttp_20220303.R
import com.jc.apipractice_okhttp_20220303.data.ReplyData
import com.jc.apipractice_okhttp_20220303.data.TopicData
import java.text.SimpleDateFormat
import java.util.*

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

        // 임시 1 - 작성 일자만 "2022-03-10" 형태로 표현 => "연 / 월 / 일" 데이터로 가공
        // 월은 1 작게 나온다. +1 로 보정
//        txtCreatedAt.text = "${data.createdAt.get(Calendar.YEAR)}-${data.createdAt.get(Calendar.MONTH) + 1}-${data.createdAt.get(Calendar.DAY_OF_MONTH)}"

        // 실제 활용
        // 임시 2 - "2022-03-10" 형태로 표현 => SimpleDateFormat 활용
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        // SimpleDateFormat (Date 객체) => 지정해둔 양식의 String 으로 가공.
        // createAt 은 Calendar / format 의 파라미터 : Data => Calendar 의 내용물인 time 변수가 Date 타입
        txtCreatedAt.text = sdf.format(data.createdAt.time)

        return row

    }

}