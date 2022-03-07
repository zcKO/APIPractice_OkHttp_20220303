package com.jc.apipractice_okhttp_20220303.adapters

import android.content.Context
import android.widget.ArrayAdapter
import com.jc.apipractice_okhttp_20220303.data.TopicData

class TopicAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<TopicData>
) : ArrayAdapter<TopicData>(mContext, resId, mList) {



}