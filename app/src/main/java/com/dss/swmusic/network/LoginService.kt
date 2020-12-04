package com.dss.swmusic.network

import com.dss.swmusic.network.bean.CheckPhoneResult
import com.dss.swmusic.network.bean.LoginResult
import com.dss.swmusic.network.bean.ResetPwResult
import com.dss.swmusic.network.bean.SendVerifyCodeResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 登录相关的网络请求
 */
interface LoginService {

    /**
     * 检查手机号是否存在
     */
    @GET("/cellphone/existence/check")
    fun checkPhoneExist(@Query("phone") phone: String): Call<CheckPhoneResult>

    /**
     * 用手机号和密码登录
     */
    @GET("/login/cellphone")
    fun login(@Query("phone") phone: String,
              @Query("password") password: String): Call<LoginResult>

    /**
     * 发送验证码
     */
    @GET("/captcha/sent")
    fun sendVerifyCode(@Query("phone") phone: String
                       ,@Query("timestamp")timestamp:String): Call<SendVerifyCodeResult>

    /**
     * 验证验证码
     */
    @GET("/captcha/verify")
    fun checkVerifyCode(@Query("phone") phone: String, @Query("captcha") captcha: Int): Call<SendVerifyCodeResult>

    /**
     * 重设密码的网络请求
     */
    @GET("/register/cellphone")
    fun resetPassword(@Query("phone") phone: String,
                      @Query("password") password:String,
                      @Query("captcha") captcha: String):Call<ResetPwResult>

}