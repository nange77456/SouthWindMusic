package com.dss.swmusic.network.bean

/**
 * “首页-发现”的推荐歌单 的返回结果数据类
 */
data class RecommendPlayListResult(
    var result: MutableList<RecommendPlayList>
):Result()

data class RecommendPlayList(
        /**
         * 歌单的id
         */
        var id: Long,
        /**
         * 歌单名
         */
        var name: String,
        /**
         * 歌单封面
         */
        var picUrl: String,
        /**
         * 歌单里歌曲数量
         */
        var trackCount: Int
)
