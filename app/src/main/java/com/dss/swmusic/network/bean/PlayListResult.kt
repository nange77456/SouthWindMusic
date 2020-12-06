package com.dss.swmusic.network.bean

/**
 * 获取歌单 的网络请求 的返回结果数据类
 */
data class PlayListResult(
        /**
         * 200 代表没问题
         */
        var code: Int,
        var playlist: List<PlayList>
)

/**
 * 歌单数据
 */
data class PlayList(
        var id: Long,
        var name: String,
        var coverImgUrl: String,
        var creator: Creator,
        /**
         * 歌单里歌曲总数
         */
        var trackCount: Int
)

/**
 * 歌单 创建者 的数据类
 */
data class Creator(
        var avatarUrl: String,
        var nickname: String,
        var userId: Long
)
