package com.dss.swmusic.network.bean

/**
 * 发送 获取歌单、收藏 等数量 的网络请求的返回数据类
 */
data class CountResult(
        /**
         * 用户创建的歌单的数量
         */
        var createdPlaylistCount:Int,
        /**
         * 用户收藏的歌单的数量
         */
        var subPlaylistCount: Int

):Result()
