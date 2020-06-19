package com.example.encryptocam.di

import com.example.encryptocam.domain.encryption.EncryptionRepository
import com.example.encryptocam.domain.encryption.EncryptionRepositoryImpl
import com.example.encryptocam.domain.files.FilesRepository
import com.example.encryptocam.domain.files.FilesRepositoryImpl
import com.example.encryptocam.domain.login.LoginRepository
import com.example.encryptocam.domain.login.LoginRepositoryImpl
import org.koin.dsl.module

/**
 * Koins representation of modules that are to be injected.
 * ModelModules contains all non ui (fragments) and view(viewmodel) related injectable
 * */
val modelModules = module {
    single<LoginRepository> { LoginRepositoryImpl(get()) }
    single<FilesRepository> { FilesRepositoryImpl(get()) }
    single<EncryptionRepository> { EncryptionRepositoryImpl() }
}