package com.example.encryptocam.commons.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.fragment.android.setupKoinFragmentFactory

abstract class BaseActivity(@LayoutRes private val layout: Int, private val args: Bundle? = null) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        setupKoinFragmentFactory()
    }
}