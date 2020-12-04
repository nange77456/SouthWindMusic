package com.dss.swmusic.network.bean

/**
 * 发送验证码 或 验证验证码 的网络请求的返回结果
 */
data class SendVerifyCodeResult(
        /**
         * 200 代表没毛病
         */
        var code:Int,
        var data:String
)
