package com.dss.swmusic.network.bean

data class DailyRecommendPlayListResult(
    var recommend: MutableList<DailyRecommendPlayList>
):Result()

data class DailyRecommendPlayList(
        var id: Long,
        /**
         * 歌单名
         */
        var name: String,
        /**
         * 封面图
         */
        var picUrl: String,
        /**
         * 创建者信息
         */
        var creator: Creator
)
