package com.dss.swmusic.network.bean

/**
 * 发送登录 网络请求 返回的数据类
 */
data class LoginResult(
        var account: Account,
        var profile: Profile,
        var cookie: String
):Result()

data class Account(
        var id: Long
)

data class Profile(
        var backgroundUrl: String,
        var nickname: String,
        var birthday: Long,
        var avatarUrl: String,
        var gender: Int,
        var signature: String
)
