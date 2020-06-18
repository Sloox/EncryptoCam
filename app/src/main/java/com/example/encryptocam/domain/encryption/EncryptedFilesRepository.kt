package com.example.encryptocam.domain.encryption

import java.io.ByteArrayOutputStream
import java.io.File

interface EncryptedFilesRepository {
    val ENCRYPTED_EXTENSION: String
    val FILENAME_FORMAT: String


    fun createFileNew(rootFolder: File): File
    fun listEncryptedFiles(rootFolder: File): List<File>
    fun savePicture(bytes: ByteArrayOutputStream)
}