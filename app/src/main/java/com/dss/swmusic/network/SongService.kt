package com.dss.swmusic.network

import com.dss.swmusic.network.bean.*
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
                          @Query("cookie") cookie: String,
                          @Query("timestamp") timestamp: Long = System.currentTimeMillis())
            : Call<PlayListDetailResult>

    /**
     * 获取歌曲详情
     */
    @GET("/song/detail")
    fun getSongDetail(@Query("ids") ids: String,
                      @Query("timestamp") timestamp: Long = System.currentTimeMillis())
            : Call<SongDetailResult>

    /**
     * 获取歌词
     */
    @GET("/lyric")
    fun getLyric(@Query("id") id: Long): Call<LyricResult>


    /**
     * 添加歌曲到歌单
     */
    @GET("/playlist/tracks?op=add")
    fun addToPlaylist(@Query("pid") playListId: Long,
                      @Query("tracks") songId: Long,
                      @Query("cookie") cookie: String)
            : Call<UpdatePlaylistResult>

    /**
     * 从歌单中删除
     */
    @GET("/playlist/tracks?op=del")
    fun deleteFromPlaylist(@Query("pid") playListId: Long,
                           @Query("tracks") songId: Long,
                           @Query("cookie") cookie: String)
            : Call<UpdatePlaylistResult>

    /**
     * 新建歌单
     */
    @GET("/playlist/create")
    fun newPlaylist(@Query("cookie") cookie: String,
                    @Query("name") name: String): Call<NewPlayListResult>


}