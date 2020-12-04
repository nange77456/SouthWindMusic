package com.dss.swmusic

import android.app.Activity
import java.util.*

object ActivityCollector {

    private val activitys = LinkedList<Activity>()

    fun addActivity(activity: Activity){
        activitys.add(activity)
    }

    fun removeActivity(activity:Activity){
        activitys.remove(activity)
    }

    fun finishAll(){
        for(activity in activitys){
            if(!activity.isFinishing){
                activity.finish()
            }
        }
        activitys.clear()
    }

    /**
     * 销毁其他 Activity
     */
    fun finishOthers(currentActivity:Activity){
        for(activity in activitys){
            if(!activity.isFinishing && activity != currentActivity){
                activity.finish()
            }
        }
    }

}