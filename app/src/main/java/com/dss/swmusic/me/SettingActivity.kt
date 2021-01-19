package com.dss.swmusic.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dss.swmusic.databinding.ActivitySettingBinding
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar.setNavigationOnClickListener {
            finish()
        }

    }
}