package com.example.encryptocam.di

import com.example.encryptocam.domain.login.LoginRepository
import com.example.encryptocam.domain.login.LoginRepositoryImpl
import org.koin.dsl.module

/**
 * Koins representation of modules that are to be injected.
 * ModelModules contains all non ui (fragments) and view(viewmodel) related injectables
 * */
val modelModules = module {
    single<LoginRepository> { LoginRepositoryImpl() }
}