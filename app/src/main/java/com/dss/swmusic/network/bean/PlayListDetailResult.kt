package com.dss.swmusic.network.bean

/**
 * 获取歌单详情 返回的数据类
 */
data class PlayListDetailResult(
        var playlist: PlayListDetail
):Result(){

}

data class PlayListDetail(
        var creator: Creator,
        /**
         * 歌曲的id都在这里面
         */
        var trackIds: List<TrackId>,
        /**
         * 封面图
         */
        var coverImgUrl: String,
        /**
         * 歌曲总数
         */
        var trackCount: Int,
        /**
         * 歌单名
         */
        var name: String,
        /**
         * 歌单的id
         */
        var id: Long,
        /**
         * 歌单描述
         */
        var description: String

)

data class TrackId(
        var id:Long
)