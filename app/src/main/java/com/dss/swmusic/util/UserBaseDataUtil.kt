package com.dss.swmusic.util

import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.dss.swmusic.MyApplication
import com.dss.swmusic.entity.UserBaseData
import com.dss.swmusic.network.bean.LoginResult


object UserBaseDataUtil {

    private var userBaseData : UserBaseData? = null

    private var cookie: String? = null

    /**
     * 用户基本数据存储的文件名，存储方式为SharedPreference
     */
    private const val USER_BASE_DATA_FILE_NAME = "user_base_data"

    /**
     * 判断用户是否登录
     */
    @JvmStatic
    fun isLogin():Boolean{
        if(userBaseData == null){
            getUserBaseData()
        }
        return cookie != null
    }

    /**
     * 获取用户的cookie
     */
    @JvmStatic
    fun getCookie():String{
        if(userBaseData == null){
            getUserBaseData()
        }
        if(cookie == null){
            cookie = userBaseData!!.cookie
        }
        return cookie!!
    }

    /**
     * 获取用户基本数据
     */
    @JvmStatic
    fun getUserBaseData():UserBaseData{
        if(userBaseData != null){
            return userBaseData!!
        }
        val sp = MyApplication.getContext()
                .getSharedPreferences(USER_BASE_DATA_FILE_NAME, MODE_PRIVATE)
        val uid = sp.getLong("uid",0)
        cookie  = sp.getString("cookie",null)
        userBaseData = UserBaseData(uid,cookie)

        userBaseData!!.nickname = sp.getString("nickname",null)
        userBaseData!!.birthday = sp.getLong("birthday",0)
        userBaseData!!.avatarUrl = sp.getString("avatarUrl",null)
        userBaseData!!.backgroundUrl = sp.getString("backgroundUrl",null)
        userBaseData!!.signature = sp.getString("signature",null)
        userBaseData!!.level = sp.getInt("level",0)

        return userBaseData!!
    }

    /**
     *
     * 将用户基本数据保存到内存和文件中
     */
    @JvmStatic
    fun setUserBaseData(data:UserBaseData){
        userBaseData = data
        saveUserBaseData(data)
    }

    /**
     * 用登录的返回结果设置用户基本数据
     */
    @JvmStatic
    fun setUserBaseData(data:LoginResult){
        Log.e("tag","setData cookie=${data.cookie}")
        userBaseData = UserBaseData(data.account.id,data.cookie)
        userBaseData!!.gender = data.profile.gender
        userBaseData!!.nickname = data.profile.nickname
        userBaseData!!.birthday = data.profile.birthday
        userBaseData!!.avatarUrl = data.profile.avatarUrl
        userBaseData!!.backgroundUrl = data.profile.backgroundUrl
        userBaseData!!.signature = data.profile.signature

        saveUserBaseData(userBaseData!!)
    }

    /**
     * 将用户基本数据保存到文件中
     */
    private fun saveUserBaseData(data:UserBaseData){
        val sp = MyApplication.getContext()
                .getSharedPreferences(USER_BASE_DATA_FILE_NAME,MODE_PRIVATE)
        val editor = sp.edit()

        editor.putLong("uid",data.uid)
        editor.putString("cookie",data.cookie)
        editor.putInt("gender",data.gender)
        editor.putString("nickname",data.nickname)
        editor.putLong("birthday",data.birthday)
        editor.putString("avatarUrl",data.avatarUrl)
        editor.putString("backgroundUrl",data.backgroundUrl)
        editor.putString("signature",data.signature)
        editor.putInt("level",data.level)

        editor.apply()
    }

}
