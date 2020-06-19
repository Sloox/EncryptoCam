package com.example.encryptocam.screens.gallery

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.files.FilesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import java.io.File

class GalleryViewModel : BaseFragmentViewModel() {

    private val context: Context by inject()
    private val fileRepository: FilesRepository by inject()
    private val _galleryPictures: MutableLiveData<List<File>> = MutableLiveData()
    val galleryPictures: LiveData<List<File>> = _galleryPictures

    override fun onAttach() {
        super.onAttach()
        GlobalScope.launch {
            loadPictures()
        }
    }

    private fun loadPictures() {
        val rootDir = fileRepository.getRootDirectory(context)
        val fileList = fileRepository.listFiles(rootDir)
        _galleryPictures.postValue(fileList)
    }

    fun onPictureClicked(clickedFile: File) {
        //do nothing for now but left as a placeholder to show how redirects to onClicks occur with this architecture
    }
}