package com.dss.swmusic.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import com.dss.swmusic.network.bean.Song

class SongDiffCallback: DiffUtil.ItemCallback<Song>(){
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.equals(newItem)
    }
}