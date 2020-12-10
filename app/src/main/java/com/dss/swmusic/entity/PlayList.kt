package com.dss.swmusic.entity

import com.dss.swmusic.network.bean.PlayListDetail
import com.dss.swmusic.network.bean.Song

/**
 * 歌单 数据类
 */
data class PlayList(
        var playListDetail: PlayListDetail,
        var songs : MutableList<Song>
)
