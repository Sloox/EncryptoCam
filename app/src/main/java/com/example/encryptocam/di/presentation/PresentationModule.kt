package com.example.encryptocam.di.presentation

import android.app.Activity
import android.content.Context
import com.example.encryptocam.domain.encryption.EncryptionService
import com.example.encryptocam.domain.encryption.EncryptionServiceImpl
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.domain.files.FilesServiceImpl
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginServiceImpl
import dagger.Module
import dagger.Provides

@Module
class PresentationModule(private val mActivity: Activity) {
    @Provides
    fun getActivity(): Activity = mActivity

    @Provides
    fun context(activity: Activity): Context = activity

    @Provides
    fun getFileService(encryptionService: EncryptionService): FilesService = FilesServiceImpl(encryptionService)

    @Provides
    fun getEncryptionService(): EncryptionService = EncryptionServiceImpl()

    @Provides
    fun getLoginService(encryptionService: EncryptionService): LoginService = LoginServiceImpl(encryptionService)

}