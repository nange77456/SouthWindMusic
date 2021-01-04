package com.dss.swmusic.network

import com.dss.swmusic.network.bean.DefaultSearchKeyResult
import com.dss.swmusic.network.bean.HotSearchListResult
import com.dss.swmusic.network.bean.SearchSongResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 搜索相关的网络请求
 */
interface SearchService {

    /**
     * 获取默认搜索词
     */
    @GET("/search/default")
    fun getDefaultSearchKey(@Query("cookie") cookie: String,
                            @Query("timestamp") timestamp: Long = System.currentTimeMillis())
            : Call<DefaultSearchKeyResult>


    /**
     * 获取热搜列表
     */
    @GET("/search/hot")
    fun getHotSearchList(): Call<HotSearchListResult>


    /**
     * 根据关键词搜索歌曲
     */
    @GET("/search")
    fun searchSong(@Query("keywords") keywords: String,
                   @Query("offset") offset: Int = 0,
                   @Query("limit") limit: Int = 30)
            : Call<SearchSongResult>

}