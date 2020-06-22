package com.example.encryptocam.di

import com.example.encryptocam.domain.encryption.EncryptionService
import com.example.encryptocam.domain.encryption.EncryptionServiceImpl
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.domain.files.FilesServiceImpl
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginServiceImpl
import org.koin.dsl.module

/**
 * Koins representation of modules that are to be injected.
 * ModelModules contains all non ui (fragments) and view(viewmodel) related injectables
 * */
val modelModules = module {
    single<LoginService> { LoginServiceImpl(get()) }
    single<FilesService> { FilesServiceImpl(get()) }
    single<EncryptionService> { EncryptionServiceImpl() }
}