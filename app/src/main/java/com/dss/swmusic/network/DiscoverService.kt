package com.dss.swmusic.network

import com.dss.swmusic.network.bean.BannerResult
import com.dss.swmusic.network.bean.DailyRecommendPlayListResult
import com.dss.swmusic.network.bean.DailyRecommendSongsResult
import com.dss.swmusic.network.bean.RecommendPlayListResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * "首页-发现“相关的网络请求
 */
interface DiscoverService {

    /**
     * 获取轮播图数据
     */
    @GET("/banner?type=1")
    fun getBanner(): Call<BannerResult>

    /**
     * 获取6个推荐歌单
     */
//    @GET("/personalized?limit=6")
    @GET("/personalized")
    fun getRecommendPlayList(@Query("cookie") cookie: String)
            : Call<RecommendPlayListResult>

    /**
     * 获取每日推荐歌单
     */
    @GET("/recommend/resource")
    fun getDailyRecommendPlayList(@Query("cookie") cookie: String)
            : Call<DailyRecommendPlayListResult>

    /**
     * 获取每日推荐歌曲
     */
    @GET("/recommend/songs")
    fun getDailyRecommendSongs(@Query("cookie") cookie: String)
            : Call<DailyRecommendSongsResult>

}