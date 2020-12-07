package com.dss.swmusic.network.bean

/**
 * 发送验证码 或 验证验证码 的网络请求的返回结果
 */
data class SendVerifyCodeResult(
        var data:String
):Result()
