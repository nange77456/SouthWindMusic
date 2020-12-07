package com.dss.swmusic.network

import android.util.Log
import com.dss.swmusic.MyApplication
import com.dss.swmusic.R
import com.dss.swmusic.network.bean.Result
import com.dss.swmusic.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 封装网络请求回调，将其分为各种情况
 */
open class OkCallback<T : Result> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if(response.isSuccessful){
            if(response.body()!!.code == 200){
                onSuccess(response.body()!!)
            }else{
                onError(response.body()!!.code,response)
            }
        }else{
            onError(response.code(),response)
            onFailureFinally()
        }
        onFinally()
    }

    /**
     * 网络请求错误时会调用此方法，code 不是2xx
     * 网络请求异常不调用此方法
     */
    open fun onError(code:Int,response: Response<T>){
        showToast("错误：${response.code()}")
    }

    /**
     * 网络请求成功时会调用此方法
     */
    open fun onSuccess(result: T){

    }

    /**
     * 网络不成功（不管是异常还是错误），最终一定会调用此方法
     */
    open fun onFailureFinally(){

    }

    /**
     * 不管成功还是失败，最终一定会调用此方法
     */
    open fun onFinally(){

    }

    /**
     * 网络请求异常时调用此方法
     */
    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailureFinally()
        onFinally()
        showToast(MyApplication.getContext().getString(R.string.network_exception_tip))
    }
}