package com.example.encryptocam.commons.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.example.encryptocam.application.EncryptoCamApplication
import com.example.encryptocam.di.application.EncryptCamAppComponent
import com.example.encryptocam.di.presentation.PresentationComponent
import com.example.encryptocam.di.presentation.PresentationModule

abstract class BaseActivity(@LayoutRes private val layout: Int, private val args: Bundle? = null) : AppCompatActivity() {
    private var mIsInjectorUsed = false

    @UiThread
    protected fun getPresentationComponent(): PresentationComponent {
        if (mIsInjectorUsed) {
            throw RuntimeException("there is no need to use injector more than once")
        }
        mIsInjectorUsed = true
        return getApplicationComponent().newPresentationComponent(PresentationModule(this))
    }

    private fun getApplicationComponent(): EncryptCamAppComponent {
        return (application as EncryptoCamApplication).getEncryptCamAppComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
    }
}