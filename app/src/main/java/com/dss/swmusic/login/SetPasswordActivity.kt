package com.dss.swmusic.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.databinding.ActivitySetPasswordBinding
import kotlinx.android.synthetic.main.activity_set_password.*

class SetPasswordActivity : BaseActivity() {

    private lateinit var binding: ActivitySetPasswordBinding

    /**
     * 手机号码，从上一个Activity传来
     */
    private var phoneNumber:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySetPasswordBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 登录的手机号码
        phoneNumber = intent.getStringExtra(DATA_KEY_PHONE)!!
        // 输入框获取焦点
        passwordEditText.requestFocus()
    }

    /**
     * ”下一步 ” 按钮的点击事件
     */
    fun onClickNext(v: View){
        val password = passwordEditText.text.toString()
        if(password == ""){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show()
            return
        }
        // 跳转到输入验证码页面
        VerifyCodeActivity.start(this,phoneNumber,password)
    }

    companion object{

        private const val DATA_KEY_PHONE = "phone"

        /**
         * 启动这个 Activity
         */
        fun start(activity: Activity, phone:String){
            val intent = Intent(activity,SetPasswordActivity::class.java)
            intent.putExtra(DATA_KEY_PHONE,phone)
            activity.startActivity(intent)
        }
    }
}