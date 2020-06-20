package com.example.encryptocam.viewmodel

import android.content.Context
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.screens.gallery.GalleryViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GalleryViewModelTest {
    @Mock
    lateinit var fileService: FilesService

    @Mock
    lateinit var context: Context

    lateinit var galleryViewModel: GalleryViewModel

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val file = File("")
        Mockito.`when`(fileService.getRootDirectory(any())).thenReturn(file)
        Mockito.`when`(fileService.listFiles(any())).thenReturn(arrayListOf())
        this.galleryViewModel = GalleryViewModel(context, fileService)
    }

    @Test
    fun `Test load pictures`() = runBlocking {
        galleryViewModel.loadPictures()
        verify(fileService).getRootDirectory(context)
        verify(fileService).listFiles(any())
        assert(galleryViewModel.galleryPictures.value == arrayListOf<File>())
    }

}