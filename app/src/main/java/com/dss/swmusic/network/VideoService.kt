package com.dss.swmusic.network

import com.dss.swmusic.network.bean.RecommendVideoResult
import com.dss.swmusic.network.bean.VideoUrlResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoService {

    /**
     * 获取推荐视频
     */
    @GET("/video/timeline/recommend")
    fun getRecommendVideo(@Query("offset") offset: Int,
                          @Query("cookie") cookie: String,
                          @Query("timestamp") timestamp: Long)
            : Call<RecommendVideoResult>


    /**
     * 获取视频播放地址
     */
    @GET("/video/url")
    fun getVideoUrl(@Query("id")id:String):Call<VideoUrlResult>

}