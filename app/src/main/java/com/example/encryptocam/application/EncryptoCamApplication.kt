package com.example.encryptocam.application

import android.app.Application
import com.example.encryptocam.di.application.DaggerEncryptCamAppComponent
import com.example.encryptocam.di.application.EncryptCamAppComponent
import com.example.encryptocam.di.application.EncryptoCamModule


class EncryptoCamApplication : Application() {
    private lateinit var mEncryptoCamAppComponent: EncryptCamAppComponent
    internal fun getEncryptCamAppComponent() = mEncryptoCamAppComponent

    override fun onCreate() {
        super.onCreate()
        mEncryptoCamAppComponent = DaggerEncryptCamAppComponent.builder()
            .encryptoCamModule(EncryptoCamModule(this))
            .build()
    }
}