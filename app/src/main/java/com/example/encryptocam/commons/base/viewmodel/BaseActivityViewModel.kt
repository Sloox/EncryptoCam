package com.example.encryptocam.commons.base.viewmodel

import androidx.lifecycle.ViewModel

/**
 * BaseView model that works with Koin dependancy injection & Works with AndroidX Navigation
 * */
abstract class BaseActivityViewModel : ViewModel() {
    open fun init() {}
}