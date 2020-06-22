package com.example.encryptocam.domain.encryption

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * Encryption implementation that makes use of MD5 to create a 128bit key to use a standard AES implementation
 * provided by the basic java classes. It is important to take note that NOT_A_PASSWORD contained within the companion object is not an ideal implementation
 * of a private key. Please @see NOT_A_PASSWORD for more details regarding this
 * */
class EncryptionServiceImpl : EncryptionService {
    override val encryptionAlgorithm: String = "AES"
    private val key: SecretKey

    init {
        key = SecretKeySpec(getEncryptionKey(), encryptionAlgorithm) //MD5 creates a 128bit key
    }

    override fun getEncryptionKey(): ByteArray {
        return doMD5(Base64.decode(NOT_A_PASSWORD, 0))
    }

    private fun doMD5(bytes: ByteArray): ByteArray {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        md.reset()
        md.update(bytes)
        return md.digest()
    }

    override fun validateInputAgainstKey(bytes: ByteArray): Boolean {
        return getEncryptionKey().contentEquals(doMD5(bytes))
    }

    override fun encryptBytes(bytes: ByteArray): ByteArray {
        if (bytes.isEmpty()) return byteArrayOf()
        val aes = Cipher.getInstance(encryptionAlgorithm)
        aes.init(Cipher.ENCRYPT_MODE, key)
        val outputStream = ByteArrayOutputStream(bytes.size)
        val cipherOut = CipherOutputStream(outputStream, aes)
        cipherOut.write(bytes)
        cipherOut.flush()
        cipherOut.close()
        return outputStream.toByteArray()
    }

    override fun decryptBytes(bytes: ByteArray): ByteArray {
        if (bytes.isEmpty()) return byteArrayOf()
        val aes = Cipher.getInstance(encryptionAlgorithm)
        aes.init(Cipher.DECRYPT_MODE, key)
        val outputStream = ByteArrayOutputStream(bytes.size)
        val cipherOut = CipherOutputStream(outputStream, aes)
        cipherOut.write(bytes)
        cipherOut.flush()
        cipherOut.close()
        return outputStream.toByteArray()
    }

    companion object {
        /**
         * Ultimately there is no truly safe way to keep a password within android source code even after its compilation.
         * The two options are large semi-custom obfuscation(native code, proguard, unconventional coding practice, odd naming etc) or
         * To simply keep it on seperate server. For simplicities sake its base64 encoded here to add atleast some sort of obfuscation
         * The password is however infact: mysafepass
         * */
        private const val NOT_A_PASSWORD = "bXlzYWZlcGFzcw=="
    }
}