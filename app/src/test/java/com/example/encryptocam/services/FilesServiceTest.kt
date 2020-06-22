package com.example.encryptocam.services

import android.content.Context
import android.content.res.Resources
import com.example.encryptocam.domain.encryption.EncryptionService
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.domain.files.FilesServiceImpl
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.io.File


@RunWith(MockitoJUnitRunner::class)
class FilesServiceTest {
    lateinit var filesService: FilesService

    private val pictureBytes = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private val encryptedBytes = byteArrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

    private val encryptionService: EncryptionService = mock<EncryptionService> {
        on { encryptBytes(anyOrNull()) } doReturn (encryptedBytes)
        on { decryptBytes(anyOrNull()) } doReturn (pictureBytes)
    }

    private val contentReturnString = "\\test"

    /*Order of instantiation is important*/
    private val mockContextResources: Resources = mock<Resources> {
        on { getString(anyInt()) } doReturn (contentReturnString)
    }

    private val mockApplicationContext = mock<Context> {
        on { resources } doReturn (mockContextResources)
    }

    private val context = mock<Context> {
        on { externalMediaDirs } doReturn (listOf(File("/")).toTypedArray())
        on { applicationContext } doReturn (mockApplicationContext)
        on { resources } doReturn (mockContextResources)
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.filesService = FilesServiceImpl(encryptionService)
    }

    @Test
    fun `Test getRootDir`() {
        val file = filesService.getRootDirectory(context)
        Assert.assertNotNull(file)
        assert(file.path == contentReturnString)
    }

    @Test
    fun `Test create new File`() {
        val file = filesService.getRootDirectory(context)
        val newFile = filesService.createFileNew(file)
        Assert.assertNotNull(newFile)
        assert(newFile.name.contains(filesService.encryptionExtenstion))
        assert(newFile.path.contains(file.path))
    }

    @Test
    fun `Test list Files`() {
        val file = filesService.getRootDirectory(context)
        val listfiles = filesService.listFiles(file)
        assert(listfiles.isEmpty())
    }

    @Test
    fun `Test save picture`() {
        /*These need to be run as elevated privileges to actually write to file therefore it will fail*/
        val file = filesService.getRootDirectory(context)
        val listfiles1 = filesService.listFiles(file)
        assert(listfiles1.isEmpty())
        filesService.savePicture(file, pictureBytes)
        verify(encryptionService).encryptBytes(anyOrNull())
        val listfiles2 = filesService.listFiles(file)
        assert(listfiles2.isNotEmpty())
    }


    @Test
    fun `Test retrieve picture`() {
        /*These need to be run as elevated privileges to actually write to file*/
        val file = filesService.getRootDirectory(context)
        filesService.savePicture(file, pictureBytes)
        verify(encryptionService).encryptBytes(anyOrNull())
        val pictureFile = filesService.getPicture(file)
        verify(encryptionService).decryptBytes(anyOrNull())
        assert(pictureFile.contentEquals(pictureBytes))
    }


}