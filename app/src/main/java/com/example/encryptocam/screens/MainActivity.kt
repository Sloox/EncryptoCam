package com.example.encryptocam.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.activity.BaseActivity

class MainActivity : BaseActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onBackPressed() = Unit //left blank intentionally

    companion object {
        fun startThisActivity(context: Context) = context.startActivity(
            Intent(context, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
