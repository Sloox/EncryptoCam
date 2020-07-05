package com.example.encryptocam.commons.base.viewmodel

import androidx.lifecycle.ViewModel
import com.example.encryptocam.commons.livedata.SingleLiveDataEvent
import com.example.encryptocam.navigation.NavCommand

/**
 * BaseView model that works with Koin dependency injection & Works with AndroidX Navigation
 * */
abstract class BaseFragmentViewModel : ViewModel() {
    val navCommand: SingleLiveDataEvent<NavCommand> = SingleLiveDataEvent()

    fun navigate(command: NavCommand) {
        navCommand.postValue(command)
    }

    open fun onAttach() {}
    open fun onDetach() {}
    open fun init() {}
}