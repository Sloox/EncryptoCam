package com.example.encryptocam.screens.gallery

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.files.FilesService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class GalleryViewModel @Inject constructor(private val context: Context, private val fileService: FilesService) : BaseFragmentViewModel() {
    private val _galleryPictures: MutableLiveData<List<File>> = MutableLiveData()
    val galleryPictures: LiveData<List<File>> = _galleryPictures

    override fun onAttach() {
        super.onAttach()
        GlobalScope.launch {
            loadPictures()
        }
    }

    @VisibleForTesting
    fun loadPictures() {
        val rootDir = fileService.getRootDirectory(context)
        val fileList = fileService.listFiles(rootDir)
        _galleryPictures.postValue(fileList)
    }

    fun onPictureClicked(clickedFile: File) {
        //do nothing for now but left as a placeholder to show how redirects to onClicks occur with this architecture
    }
}