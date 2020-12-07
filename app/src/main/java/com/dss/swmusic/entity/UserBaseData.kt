package com.dss.swmusic.entity

/**
 * 用户基本数据
 */
data class UserBaseData(
        var uid:Long,
        /**
         * 用户登录的cookie数据
         */
        var cookie: String?
){
        var gender:Int = 0
        var nickname:String? = null
        var birthday:Long = 0
        var avatarUrl:String? = null
        var backgroundUrl:String? = null
        var signature: String? = null
        /**
         * 用户等级
         */
        var level: Int = 0
}