package com.example.encryptocam.commons.base.viewmodel

import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

/**
 * BaseView model that works with Koin dependancy injection & Works with AndroidX Navigation
 * */
abstract class BaseActivityViewModel : ViewModel(), KoinComponent {
    open fun init() {}
}