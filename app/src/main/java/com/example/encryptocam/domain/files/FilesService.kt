package com.example.encryptocam.domain.files

import android.content.Context
import java.io.File

interface FilesService {
    val encryptionExtenstion: String
    val fileFormat: String


    fun getRootDirectory(context: Context): File
    fun createFileNew(rootFolder: File): File
    fun listFiles(rootFolder: File): List<File>

    fun savePicture(file: File, bytes: ByteArray)
    fun getPicture(file: File): ByteArray
}