package com.dss.swmusic.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.MainActivity
import com.dss.swmusic.databinding.ActivityPasswordBinding
import com.dss.swmusic.entity.UserBaseData
import com.dss.swmusic.network.LoginService
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.LoginResult
import com.dss.swmusic.util.UserBaseDataUtil
import kotlinx.android.synthetic.main.activity_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : BaseActivity() {

    private lateinit var binding:ActivityPasswordBinding

    /**
     * 网络请求类
     */
    private val loginService = ServiceCreator.create<LoginService>()

    /**
     * 手机号码，从上一个Activity传来
     */
    private var phoneNumber:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 登录的手机号码
        phoneNumber = intent.getStringExtra(DATA_KEY_PHONE)!!

        // 输入框获取焦点
        passwordEditText.requestFocus()
    }

    /**
     * ”忘记密码“按钮的点击事件
     */
    fun onClickForgetPW(v:View){
        // 跳转到设置密码页面
        SetPasswordActivity.start(this,phoneNumber)
    }

    /**
     * 点击"登录"按钮时
     */
    fun onClickOkButton(v: View){
        // 获取输入的密码
        val password = passwordEditText.text.toString();
        // 检查是否为空
        if(password.equals("")){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show()
            return
        }
        // 发送登录的网络请求
        loginService.login(phoneNumber,password).enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                val loginResult = response.body()

                if(loginResult?.code == 200){
                    // 登录成功
                    Toast.makeText(this@PasswordActivity,"登录成功",Toast.LENGTH_SHORT).show()
                    // 保存用户基本数据
                    UserBaseDataUtil.setUserBaseData(loginResult)

                    // 跳转到主页面
                    val intent = Intent(this@PasswordActivity,MainActivity::class.java)
                    startActivity(intent)
                }else{
                    // 登录失败
                    Toast.makeText(this@PasswordActivity,"密码错误！",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object{

        private const val DATA_KEY_PHONE = "phone"

        /**
         * 启动这个 Activity
         */
        fun start(activity: Activity, phone:String){
            val intent = Intent(activity,PasswordActivity::class.java)
            intent.putExtra(DATA_KEY_PHONE,phone)
            activity.startActivity(intent)
        }
    }

}