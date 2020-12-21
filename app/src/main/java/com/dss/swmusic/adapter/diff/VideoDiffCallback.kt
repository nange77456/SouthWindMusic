package com.dss.swmusic.adapter.diff

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.dss.swmusic.network.bean.VideoData

class VideoDiffCallback: DiffUtil.ItemCallback<VideoData>() {
    override fun areItemsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
        return oldItem.data.vid == newItem.data.vid
    }

    override fun areContentsTheSame(oldItem: VideoData, newItem: VideoData): Boolean {
        val b = oldItem.data.vid == newItem.data.vid
        Log.e("tag","same = $b")
        return oldItem.data.vid == newItem.data.vid
    }
}