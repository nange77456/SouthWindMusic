package com.dss.swmusic.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dss.swmusic.BaseActivity
import com.dss.swmusic.MainActivity
import com.dss.swmusic.databinding.ActivityVerifyCodeBinding
import com.dss.swmusic.network.LoginService
import com.dss.swmusic.network.OkCallback
import com.dss.swmusic.network.ServiceCreator
import com.dss.swmusic.network.bean.LoginResult
import com.dss.swmusic.network.bean.SendVerifyCodeResult
import com.dss.swmusic.util.UserBaseDataUtil
import com.dss.swmusic.util.showToast
import kotlinx.android.synthetic.main.activity_verify_code.*
import retrofit2.Response

class VerifyCodeActivity : BaseActivity() {

    private lateinit var binding: ActivityVerifyCodeBinding

    /**
     * 网络请求类
     */
    private val loginService = ServiceCreator.create<LoginService>()

    /**
     * 注册/登录的手机号
     */
    private lateinit var phone: String

    /**
     * 注册/登录 的密码
     */
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取上一个Activity传来的手机号和密码
        phone = intent.getStringExtra(DATA_KEY_PHONE)!!
        password = intent.getStringExtra(DATA_KEY_PASSWORD)!!
        // 设置手机号UI
        phoneTextView.text = phone
        // 发送验证码
        sendVerifyCode(phone)

        // 开始倒计时
        reGetButton.startCountDown()
        // 点击“重新获取”
        reGetButton.onClickReGet = {
            // 再发送验证码
            sendVerifyCode(phone)
        }

        // 验证码输入框的输入完成监听
        verifyEditText.setInputCompleteListener { _, captcha ->
            // 输入完成，发送修改密码网络请求
            resetPassword(phone, password, captcha)

        }
    }

    /**
     * 发送验证码
     */
    private fun sendVerifyCode(phone: String) {
        loginService.sendVerifyCode(phone, System.currentTimeMillis().toString())
                .enqueue(object : OkCallback<SendVerifyCodeResult>() {
                    override fun onSuccess(result: SendVerifyCodeResult) {
                        super.onSuccess(result)
                        // 发送成功，什么都不干
                        Toast.makeText(this@VerifyCodeActivity, "验证码已发送", Toast.LENGTH_SHORT).show()

                    }
                })
    }

    /**
     * 重置密码的网络请求
     */
    private fun resetPassword(phone: String, password: String, captcha: String) {
        loginService.resetPassword(phone, password, captcha).enqueue(object : OkCallback<LoginResult>() {
            override fun onSuccess(result: LoginResult) {
                // 重设密码的网络请求不返回cookie，再调用一次登录（这不太合理，不过暂时先这样，下次一定改）
               // 发送登录的网络请求
                loginService.login(phone, password).enqueue(object : OkCallback<LoginResult>() {
                    override fun onSuccess(result: LoginResult) {
                        super.onSuccess(result)
                        // 保存用户基本数据
                        UserBaseDataUtil.setUserBaseData(result)
                        // 跳转到主页面
                        val intent = Intent(this@VerifyCodeActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                })
            }

            override fun onError(code: Int, response: Response<LoginResult>) {
                showToast("验证码错误", Toast.LENGTH_LONG)
            }
        })
    }

    companion object {

        private const val DATA_KEY_PHONE = "phone"
        private const val DATA_KEY_PASSWORD = "password"

        /**
         * 启动这个Activity
         */
        fun start(activity: Activity, phone: String, password: String) {
            val intent = Intent(activity, VerifyCodeActivity::class.java)
            intent.putExtra(DATA_KEY_PHONE, phone)
            intent.putExtra(DATA_KEY_PASSWORD, password)
            activity.startActivity(intent)
        }
    }

}