package com.dss.swmusic.util

import android.util.Log
import com.dss.swmusic.MyApplication
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*
import kotlin.concurrent.thread

/**
 * 磁盘缓存工具类，缓存文件保存在 外存/cache/目录下
 */
object DiskCacheUtil {

    /**
     * 异步向指定文件写入字符串
     * 如果文件不存在，会自动创建
     */
    fun writeAsync(path:String, content:String){
        thread{
            val file = File(MyApplication.getContext().externalCacheDir,path)
            val parent = file.parentFile
            if(!parent.exists()){
                parent.mkdirs()
            }
            if(!file.exists()){
                file.createNewFile()
            }
            val fw = FileWriter(file)
            val bw = BufferedWriter(fw)
            bw.write(content)

            bw.close()
        }
    }

    /**
     * 异步读取指定文件的字符串
     * 在回调中已经自动切换为主线程
     * 如果文件不存在，结果为null
     */
    fun readAsync(path:String, callback:(String?)->Unit){
        thread{
            val content = readSync(path)
            MyApplication.post{
                callback(content)
            }
        }
    }

    /**
     * 同步读取指定文件的字符串，如果不存在返回null
     */
    fun readSync(path:String):String?{
        val file = File(MyApplication.getContext().externalCacheDir,path)
        if(!file.exists()){
            return null
        }else{
            val fr = FileReader(file)
            val br = BufferedReader(fr)
            return br.readText()
        }
    }

    /**
     * 把对象转换成json字符串写入指定文件
     * 异步
     */
    fun <T> set(path:String,obj:T){
        val gson = Gson()
        writeAsync(path,gson.toJson(obj))
    }

    /**
     * 读出指定文件，并转换成指定对象
     * 异步，在回调中已自动切换为主线程
     * 如果文件不存在，返回null
     */
    inline fun <reified T> get(path:String, crossinline callback:(T?)->Unit){
        readAsync(path){
            if(it == null){
                callback(null)
            }else{
                val gson = Gson()
                val obj = gson.fromJson<T>(it, object: TypeToken<T>(){}.type)
                callback(obj)
            }
        }
    }

    /**
     * 读出指定文件，并转换成指定对象
     * 同步
     * 如果文件不存在，返回null
     */
    inline fun <reified T> getSync(path:String):T?{
        val content = readSync(path)
        val gson = Gson()
        return gson.fromJson<T>(content, object: TypeToken<T>(){}.type)
    }

}