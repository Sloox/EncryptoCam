package com.example.encryptocam.viewmodel

import android.content.Context
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.navigation.NavCommand
import com.example.encryptocam.screens.camera.CameraState
import com.example.encryptocam.screens.camera.CameraViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayOutputStream
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CameraViewModelTest {

    @Mock
    lateinit var fileService: FilesService

    @Mock
    lateinit var context: Context

    lateinit var cameraViewModel: CameraViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.cameraViewModel = CameraViewModel(context, fileService)
    }

    @Test
    fun `Test the navigation`() = runBlocking {
        cameraViewModel.navigateBack()
        assert(cameraViewModel.navCommand.value == NavCommand.LoginFragment)
        cameraViewModel.navigateToGallery()
        assert(cameraViewModel.navCommand.value == NavCommand.GalleryFragment)
        return@runBlocking
    }

    @Test
    fun `Test take picture`() = runBlocking {
        cameraViewModel.takePicture()
        assert(cameraViewModel.camCommandState.value == CameraState.TakePicture)
    }

    @Test
    fun `Test toggle flash`() = runBlocking {
        cameraViewModel.toggleFlash()
        assert(cameraViewModel.flashStateEnabled.value == true)
        cameraViewModel.toggleFlash()
        assert(cameraViewModel.flashStateEnabled.value == false)
    }

    @Test
    fun `Test toggle aem`() = runBlocking {
        cameraViewModel.toggleAEM()
        assert(cameraViewModel.aemEnabled.value == true)
        cameraViewModel.toggleAEM()
        assert(cameraViewModel.aemEnabled.value == false)
    }

    @Test
    fun `Test toggle facing camera`() = runBlocking {
        cameraViewModel.toggleCameraFacing()
        assert(cameraViewModel.frontFacingCameraSelection.value == true)
        cameraViewModel.toggleCameraFacing()
        assert(cameraViewModel.aemEnabled.value == false)
    }

    @Test
    fun `Test clear state`() = runBlocking {
        cameraViewModel.clearCameraState()
        assert(cameraViewModel.camCommandState.value == CameraState.Default)
    }

    @Test
    fun `Test Encrypt and Save Image`() = runBlocking {
        val file = File("")
        `when`(fileService.getRootDirectory(context)).thenReturn(file)
        `when`(fileService.createFileNew(file)).thenReturn(file)
        cameraViewModel.encryptAndSaveImage(ByteArrayOutputStream())
        verify(fileService).getRootDirectory(context)
        verify(fileService).createFileNew(file)
        verify(fileService).savePicture(file, ByteArrayOutputStream().toByteArray())
    }

}