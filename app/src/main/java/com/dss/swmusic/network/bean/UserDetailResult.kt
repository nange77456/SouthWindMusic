package com.dss.swmusic.network.bean

/**
 * 获取用户详情，返回的数据类
 */
data class UserDetailResult(
        /**
         * 用户等级
         */
        var level:Int,
        var profile: Profile
):Result()
