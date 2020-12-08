package com.dss.swmusic.me.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dss.swmusic.network.bean.PlayListDetail
import com.dss.swmusic.network.bean.Song

class PlayListDetailViewModel:ViewModel() {

    /**
     * 歌单详情数据
     */
    val playListDetail = MutableLiveData<PlayListDetail>()

    /**
     * 歌曲数据
     */
    val songs = MutableLiveData<MutableList<Song>>()



}