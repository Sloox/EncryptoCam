package com.example.encryptocam.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) : ViewModelProvider.Factory {
    private val mProviderMap: Map<Class<out ViewModel>, Provider<ViewModel>> = providerMap

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return (mProviderMap[modelClass])?.get() as T
    }
}