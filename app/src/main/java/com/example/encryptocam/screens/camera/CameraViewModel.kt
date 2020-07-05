package com.example.encryptocam.screens.camera

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.navigation.NavCommand
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class CameraViewModel @Inject constructor(val context: Context, private val filesService: FilesService) : BaseFragmentViewModel() {
    private val _flashStateEnabled = MutableLiveData(false)
    val flashStateEnabled: LiveData<Boolean> = _flashStateEnabled

    private val _frontFacingCameraSelection = MutableLiveData(false)
    val frontFacingCameraSelection: LiveData<Boolean> = _frontFacingCameraSelection

    private val _aemEnabled = MutableLiveData(false)
    val aemEnabled: LiveData<Boolean> = _aemEnabled

    private val _camCommandState: MutableLiveData<CameraState> = MutableLiveData(CameraState.Default)
    val camCommandState: LiveData<CameraState> = _camCommandState

    override fun onAttach() {
        super.onAttach()
        checkForGalleryPictures()
    }

    private fun checkForGalleryPictures() {
        val rootFolder = filesService.getRootDirectory(context)
        val listFiles = filesService.listFiles(rootFolder)
        if (listFiles.isNotEmpty()) {
            _camCommandState.postValue(CameraState.SetThumbnail(filesService.getPicture(listFiles.first())))
        }
    }

    fun navigateBack() {
        navigate(NavCommand.LoginFragment)
    }

    fun navigateToGallery() {
        navigate(NavCommand.GalleryFragment)
    }

    fun takePicture() {
        _camCommandState.postValue(CameraState.TakePicture)
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

    /*we use suspend here to ensure its run within coroutine scope*/
    suspend fun encryptAndSaveImage(bufferedOutputStream: ByteArrayOutputStream) {
        clearCameraState()
        val root = filesService.getRootDirectory(context)
        val file = filesService.createFileNew(root)
        filesService.savePicture(file, bufferedOutputStream.toByteArray())
    }

    fun clearCameraState() {
        _camCommandState.postValue(CameraState.Default)
    }
}