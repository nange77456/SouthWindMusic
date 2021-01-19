package com.dss.swmusic.network.bean

/**
 * 更新歌单的结果
 */
data class UpdatePlaylistResult(
        var status:Int,
        /**
         * 歌单id
         */
        var id:Long
)