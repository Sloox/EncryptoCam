package com.example.encryptocam.domain.encryption

import android.util.Base64
import java.io.ByteArrayOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class EncryptionRepositoryImpl : EncryptionRepository {
    override val ENCRYPT_ALGO: String = "AES"
    private val key: SecretKey

    init {
        key = SecretKeySpec(getEncryptionKey(), ENCRYPT_ALGO) //MD5 as 128bit key works wonders
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
        val aes = Cipher.getInstance(ENCRYPT_ALGO)
        aes.init(Cipher.ENCRYPT_MODE, key)
        val outputStream = ByteArrayOutputStream(bytes.size)
        val cipherOut = CipherOutputStream(outputStream, aes)
        cipherOut.write(bytes)
        cipherOut.flush()
        cipherOut.close()
        return outputStream.toByteArray()
    }

    override fun decryptBytes(bytes: ByteArray): ByteArray {
        val aes = Cipher.getInstance(ENCRYPT_ALGO)
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