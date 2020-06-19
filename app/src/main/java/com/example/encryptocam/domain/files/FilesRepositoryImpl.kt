package com.example.encryptocam.domain.files

import android.content.Context
import com.example.encryptocam.R
import com.example.encryptocam.domain.encryption.EncryptionRepository
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class FilesRepositoryImpl(private val encryptionRepository: EncryptionRepository) : FilesRepository {
    override val ENCRYPTED_EXTENSION: String = ".jpc"
    override val FILENAME_FORMAT: String = "yyyy-MM-dd-HH-mm-ss-SSS"

    override fun getRootDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let { File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else appContext.filesDir
    }

    override fun createFileNew(rootFolder: File): File = File(rootFolder, SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ENCRYPTED_EXTENSION)

    override fun listFiles(rootFolder: File): List<File> {
        return rootFolder.listFiles()?.asList() ?: arrayListOf()
    }

    override fun savePicture(file: File, bytes: ByteArray) {
        val fileOutputStream = FileOutputStream(file)
        val encryptedBytes = encryptionRepository.encryptBytes(bytes)
        fileOutputStream.write(encryptedBytes)
        fileOutputStream.flush()
        fileOutputStream.close()
    }

    override fun getPicture(file: File): ByteArray {
        val bytes = file.inputStream().readBytes()
        return encryptionRepository.decryptBytes(bytes)
    }


}