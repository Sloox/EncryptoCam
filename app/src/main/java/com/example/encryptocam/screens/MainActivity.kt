package com.example.encryptocam.screens

import android.os.Bundle
import android.view.WindowManager
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.activity.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
