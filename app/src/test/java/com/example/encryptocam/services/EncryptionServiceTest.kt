package com.example.encryptocam.services

import com.example.encryptocam.domain.encryption.EncryptionService
import com.example.encryptocam.domain.encryption.EncryptionServiceImpl
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class EncryptionServiceTest {
    lateinit var encryptionService: EncryptionService
    private val encryptkey = byteArrayOf(44, -79, -120, -94, 63, 48, -27, -99, 86, -32, -118, -124, 30, -119, 54, 55)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.encryptionService = EncryptionServiceImpl()
    }

    @Test
    fun `Test encryption key`() {
        val encryptionKey = encryptionService.getEncryptionKey()
        Assert.assertNotNull(encryptionKey)
        /* Ultimately direct comparison like this is silly the key should not be stored within the android application
         * It should be retrieved remotely, but that will greatly change the scope of the requirements */
        assert(encryptionKey.contentEquals(encryptkey))
    }

    @Test
    fun `Test validate wrong input against key`() {
        val result = encryptionService.validateInputAgainstKey(byteArrayOf(0, 0, 0, 0))
        assert(!result)
    }

    @Test
    fun `Test validate no input against key`() {
        val result = encryptionService.validateInputAgainstKey(byteArrayOf())
        assert(!result)
    }

    @Test
    fun `Test validate correct input against key`() {
        /* Again another not so clever comparison as the key is static */
        val result = encryptionService.validateInputAgainstKey("mysafepass".toByteArray())
        assert(result)
    }

    @Test
    fun `Test encrypt bytes for no input`() {
        val result = encryptionService.encryptBytes(byteArrayOf())
        assert(result.isEmpty())
    }

    @Test
    fun `Test encrypt bytes comparison`() {
        val result = encryptionService.encryptBytes(byteArrayOf(0, 1, 2, 3))
        assert(result.contentEquals(byteArrayOf(-128, 27, -55, 109, -71, -99, -10, 25, 67, -60, -89, 58, 72, -66, -77, -42)))
    }

    @Test
    fun `Test decrypt bytes empty`() {
        val result = encryptionService.decryptBytes(byteArrayOf())
        assert(result.isEmpty())
    }

    @Test
    fun `Test decrypt bytes comparison`() {
        val result = encryptionService.decryptBytes(byteArrayOf(-128, 27, -55, 109, -71, -99, -10, 25, 67, -60, -89, 58, 72, -66, -77, -42))
        assert(result.contentEquals(byteArrayOf(0, 1, 2, 3)))
    }

}