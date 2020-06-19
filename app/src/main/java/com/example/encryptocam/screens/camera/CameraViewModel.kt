package com.example.encryptocam.screens.camera

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.files.FilesRepository
import com.example.encryptocam.navigation.NavCommand
import org.koin.core.inject
import java.io.ByteArrayOutputStream

class CameraViewModel : BaseFragmentViewModel() {

    private val filesRepository: FilesRepository by inject()
    private val context: Context by inject()

    private val _flashStateEnabled = MutableLiveData<Boolean>(false)
    val flashStateEnabled: LiveData<Boolean> = _flashStateEnabled

    private val _frontFacingCameraSelection = MutableLiveData<Boolean>(false)
    val frontFacingCameraSelection: LiveData<Boolean> = _frontFacingCameraSelection

    private val _aemEnabled = MutableLiveData<Boolean>(false)
    val aemEnabled: LiveData<Boolean> = _aemEnabled

    private val _camCommandState: MutableLiveData<CameraState> = MutableLiveData(CameraState.Default)
    val camCommandState: LiveData<CameraState> = _camCommandState

    override fun onAttach() {
        super.onAttach()
        checkForGalleryPictures()
    }

    private fun checkForGalleryPictures() {
        val rootFolder = filesRepository.getRootDirectory(context)
        val listFiles = filesRepository.listFiles(rootFolder)
        if (listFiles.isNotEmpty()) {
            _camCommandState.postValue(CameraState.SetThumbnail(filesRepository.getPicture(listFiles.first())))
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

    /**we use suspend here to ensure its run within coroutine scope*/
    suspend fun encryptAndSaveImage(bufferedOutputStream: ByteArrayOutputStream) {
        clearCameraState()
        val root = filesRepository.getRootDirectory(context)
        val file = filesRepository.createFileNew(root)
        filesRepository.savePicture(file, bufferedOutputStream.toByteArray())
    }

    fun clearCameraState() {
        _camCommandState.postValue(CameraState.Default)
    }
}