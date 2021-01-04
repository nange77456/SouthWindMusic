package com.dss.swmusic.entity

import android.net.Uri

data class PlayerSong(
        /**
         * 类型，1代表本地资源，2代表网络资源，3代表二者都有
         */
        var type: Int,

        /**
         * 歌手信息
         */
        var artists:List<Artist>,

        /**
         * 专辑信息
         */
        var albums:Album
) {
    /**
     * uri ，如果是本地资源则有
     */
    var uri: Uri? = null

    /**
     * 歌曲名
     */
    var name: String = "UnKnow"

    /**
     * id ，如果是网络资源则有
     */
    var id: Long? = null

    /**
     * 歌曲时长，单位为毫秒
     */
    var duration: Int = 0
}

/**
 * 歌手类
 */
data class Artist(
        /**
         * 歌手名字
         */
        var name: String
) {
    /**
     * 歌手的id，如果是网络资源则有
     */
    var id: Long? = null
}

/**
 * 专辑类
 */
data class Album(
        /**
         * 专辑名
         */
    var name: String
){
    /**
     * 专辑id，如果是网络资源则有
     */
    var id: Long? = null

    /**
     * 专辑封面图，如果是网络资源则有
     */
    var picUrl:String? = null
}