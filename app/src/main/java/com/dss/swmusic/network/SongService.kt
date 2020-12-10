package com.dss.swmusic.network

import com.dss.swmusic.network.bean.PlayListDetailResult
import com.dss.swmusic.network.bean.SongDetailResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 歌曲相关的网络请求
 */
interface SongService {

    /**
     * 获取歌单详情
     */
    @GET("/playlist/detail")
    fun getPlayListDetail(@Query("id") id: Long,
                          @Query("cookie") cookie: String): Call<PlayListDetailResult>

    /**
     * 获取歌曲详情
     */
    @GET("/song/detail")
    fun getSongDetail(@Query("ids")ids:String): Call<SongDetailResult>

}