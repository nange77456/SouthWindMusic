package com.dss.swmusic.util

import `fun`.inaction.dialog.dialogs.CommonDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.dss.swmusic.entity.Artist
import com.dss.swmusic.network.bean.Ar

fun <T : View> T.height(function: (Int) -> Unit) {
    if (height == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(height)
            }
        })
    else function(height)
}

fun <T : View> T.width(function: (Int) -> Unit) {
    if (width == 0)
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                function(width)
            }
        })
    else function(width)
}

fun List<Artist>.toNiceString():String{
    var result = ""
    for(ar in this){
        result += "${ar.name}/"
    }
    return result.substring(0,result.length-1)
}

fun List<Ar>.toNiceStr():String{
    var result = ""
    for(ar in this){
        result += "${ar.name}/"
    }
    return result.substring(0,result.length-1)
}

/**
 * 显示暂未实现对话框
 */
fun showNoImplementDialog(context:Context?) {
    context?.let {
        val dialog = CommonDialog(it)
        with(dialog) {
            setTitle("提示")
            setContent("暂未实现，下次一定！")
            onConfirmClickListener = {
                dialog.dismiss()
            }
            onCancelClickListener = {
                dialog.dismiss()
            }
            show()
        }
    }
}