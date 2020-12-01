package com.dss.swmusic.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.databinding.ActivityLoginBinding
import com.dss.swmusic.network.LoginService
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.CheckPhoneResult
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class LoginActivity : BaseActivity() {

    private lateinit var binding :ActivityLoginBinding

    /**
     * 发送网络请求的类
     */
    private val loginService = ServiceCreator.create<LoginService>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置toolbar
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        // editText 获取焦点
        phoneEditText.requestFocus()

    }

    /**
     * “下一步"按钮的点击事件
     */
    fun onClickNext(v: View){
        // 获取手机号
        val phoneNumber = phoneEditText.text.toString()
        // 检查手机号是否合法
        if(!isPhoneNumber(phoneNumber)){
            Toast.makeText(this,"请输入合法手机号",Toast.LENGTH_SHORT).show()
            return
        }
        // 发送网络请求检查手机号是否注册过
        loginService.checkPhoneExist(phoneNumber).enqueue(object : Callback<CheckPhoneResult> {
            override fun onResponse(call: Call<CheckPhoneResult>, response: Response<CheckPhoneResult>) {
                val checkPhoneResult = response.body()
                if(checkPhoneResult?.exist == 1){
                    // 手机号已注册过，跳转输入密码界面
                    PasswordActivity.start(this@LoginActivity,phoneNumber)

                }else{
                    // 手机号未注册过，跳转设置密码界面
                    SetPasswordActivity.start(this@LoginActivity,phoneNumber)
                }
            }

            override fun onFailure(call: Call<CheckPhoneResult>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    /**
     * 检查该字符串是否是手机号码
     */
    private fun isPhoneNumber(number:String):Boolean{
        val regular = "\\d{11}"
        return Pattern.compile(regular).matcher(number).matches()
    }

}