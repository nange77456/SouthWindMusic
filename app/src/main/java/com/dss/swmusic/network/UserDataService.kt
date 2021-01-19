package com.dss.swmusic.network

import com.dss.swmusic.network.bean.CountResult
import com.dss.swmusic.network.bean.LevelResult
import com.dss.swmusic.network.bean.PlayListResult
import com.dss.swmusic.network.bean.UserDetailResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 用户信息相关的网络请求
 */
interface UserDataService {

    @GET("/user/detail")
    fun getUserDetail(@Query("uid") uid: Long,
                      @Query("cookie") cookie: String): Call<UserDetailResult>

    /**
     * 获取用户等级信息的网络请求
     */
    @GET("/user/level")
    fun getLevelInfo(@Query("cookie") cookie: String): Call<LevelResult>

    /**
     * 获取用户歌单数量的网络请求
     */
    @GET("/user/subcount")
    fun getPlayListCount(@Query("cookie") cookie: String): Call<CountResult>

    /**
     * 获取用户歌单基本数据的网络请求
     */
    @GET("/user/playlist")
    fun getUserPlayListInfo(@Query("cookie") cookie: String,
                            @Query("uid") uid: Long,
                            @Query("limit") limit: Int,
                            @Query("timestamp") timestamp: Long = System.currentTimeMillis())
            : Call<PlayListResult>

}