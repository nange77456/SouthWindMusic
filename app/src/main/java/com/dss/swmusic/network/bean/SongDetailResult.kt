package com.dss.swmusic.network.bean

import android.os.Parcelable

data class SongDetailResult(
    var songs: MutableList<Song>
):Result()

data class Song(
        /**
         * 歌曲名
         */
        var name:String,
        /**
         * 歌手信息
         */
        var ar: List<Ar>,
        /**
         * 专辑信息
         */
        var al: Al

)

/**
 * 代表歌手
 */
data class Ar(
        var id:Long,
        /**
         * 歌手名
         */
        var name:String
)

/**
 * 代表专辑
 */
data class Al(
        var id: Long,
        /**
         * 专辑名
         */
        var name: String,
        /**
         * 专辑封面图
         */
        var picUrl:String
)