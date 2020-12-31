package com.dss.swmusic.network.bean

data class RecommendVideoResult(
    var datas:MutableList<VideoData>
):Result()

data class VideoData(
        var data :Data
){
    data class Data(
            /**
             * url 相关信息
             */
            var urlInfo: UrlInfo,
            /**
             * 封面图
             */
            var coverUrl: String,
            /**
             * 标题
             */
            var title:String,
            /**
             * 描述
             */
            var description: String,
            /**
             * 视频的id
             */
            var vid: String
    )

    data class UrlInfo(
            var id:String,
            var url: String
    )
}