package com.example.encryptocam.domain.encryption

interface EncryptionService {
    val ENCRYPT_ALGO: String
    fun getEncryptionKey(): ByteArray
    fun validateInputAgainstKey(bytes: ByteArray): Boolean
    fun encryptBytes(bytes: ByteArray): ByteArray
    fun decryptBytes(bytes: ByteArray): ByteArray

}