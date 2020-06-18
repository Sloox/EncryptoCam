package com.example.encryptocam.screens.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.navigation.NavCommand

class CameraViewModel : BaseFragmentViewModel() {

    private val _flashStateEnabled = MutableLiveData<Boolean>(false)
    val flashStateEnabled: LiveData<Boolean> = _flashStateEnabled

    private val _frontFacingCameraSelection = MutableLiveData<Boolean>(false)
    val frontFacingCameraSelection: LiveData<Boolean> = _frontFacingCameraSelection

    private val _aemEnabled = MutableLiveData<Boolean>(false)
    val aemEnabled: LiveData<Boolean> = _aemEnabled


    fun navigateBack() {
        navigate(NavCommand.LoginFragment)
    }

    fun toggleFlash() {
        _flashStateEnabled.postValue(_flashStateEnabled.value?.not() ?: false)
    }

    fun toggleAEM() {
        _aemEnabled.postValue(_aemEnabled.value?.not() ?: false)
    }

    fun toggleCameraFacing() {
        _frontFacingCameraSelection.postValue(_frontFacingCameraSelection.value?.not() ?: false)
    }
}