package com.example.encryptocam.domain.encryption

import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EncryptedFilesRepositoryImpl : EncryptedFilesRepository {
    override val ENCRYPTED_EXTENSION: String = ".jpc"
    override val FILENAME_FORMAT: String = "yyyy-MM-dd-HH-mm-ss-SSS"

    override fun createFileNew(rootFolder: File): File = File(rootFolder, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ENCRYPTED_EXTENSION)

    override fun listEncryptedFiles(rootFolder: File): List<File> {
        return rootFolder.listFiles()?.asList() ?: arrayListOf()
    }

    override fun savePicture(bytes: ByteArrayOutputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}