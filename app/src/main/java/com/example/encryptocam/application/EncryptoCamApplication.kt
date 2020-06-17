package com.example.encryptocam.application

import android.app.Application
import org.koin.core.context.startKoin
import com.example.encryptocam.BuildConfig
import com.example.encryptocam.di.modelModules
import com.example.encryptocam.di.uiModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory

class EncryptoCamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@EncryptoCamApplication)
            fragmentFactory()
            modules(mutableListOf(modelModules, uiModules))
        }
    }
}