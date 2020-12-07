package com.dss.swmusic.network.bean

/**
 * 检查手机号是否存在的网络请求的数据类
 */
data class CheckPhoneResult(
        var exist: Int,
        var nickname: String,
        var hasPassword: Boolean,
):Result()
