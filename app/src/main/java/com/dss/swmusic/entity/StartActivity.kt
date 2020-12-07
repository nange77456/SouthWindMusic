package com.dss.swmusic.entity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.dss.swmusic.MainActivity
import com.dss.swmusic.databinding.ActivityStartBinding
import com.dss.swmusic.login.EntryActivity
import com.dss.swmusic.util.UserBaseDataUtil

/**
 * 启动页
 */
class StartActivity : AppCompatActivity() {

    private lateinit var binding:ActivityStartBinding

    private val handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(UserBaseDataUtil.isLogin()){
            val intent  = Intent(this,MainActivity::class.java)
            handler.postDelayed({
                startActivity(intent)
                finish()
            },500)

        }else{
            val intent = Intent(this,EntryActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}