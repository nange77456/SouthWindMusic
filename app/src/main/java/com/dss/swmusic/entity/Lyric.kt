package com.dss.swmusic.entity

data class Lyric(
        /**
         * 歌词的时间，毫秒单位
         */
        var time:Int,
        var lyric: String
)