package com.dss.swmusic.network.bean

/**
 * 发送登录 网络请求 返回的数据类
 */
data class LoginResult(

        /**
         * 200 表示登录成功
         */
        var code:Int,
        var account:Account,
        var profile: Profile,
        var cookie: String
)

data class Account(
        var id:String
)

data class Profile(
        var backgroundUrl:String,
        var nickName:String,
        var birthday:Long,
        var avatarUrl:String
)
