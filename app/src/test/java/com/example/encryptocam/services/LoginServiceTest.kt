package com.example.encryptocam.services

import com.example.encryptocam.domain.encryption.EncryptionService
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginServiceImpl
import com.example.encryptocam.domain.login.LoginStateEnum
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoginServiceTest {
    lateinit var loginService: LoginService
    private val encryptionService: EncryptionService = mock<EncryptionService> {
        on { validateInputAgainstKey("".toByteArray()) } doReturn (false)
        on { validateInputAgainstKey("123".toByteArray()) } doReturn (false)
        on { validateInputAgainstKey("verysafepass".toByteArray()) } doReturn (true)
    }


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        this.loginService = LoginServiceImpl(encryptionService)
    }

    @Test
    fun `Test login with empty pass`() = runBlocking {
        loginService.doLogin("")
        waitForWorkToComplete()
        assert(loginService.loginState.value == LoginStateEnum.ERROR_NO_PASSWORD)
    }

    @Test
    fun `Test login with short pass`() = runBlocking {
        loginService.doLogin("123")
        waitForWorkToComplete()
        assert(loginService.loginState.value == LoginStateEnum.ERROR_TOO_SHORT_PASS)
    }

    @Test
    fun `Test login with good pass`() = runBlocking {
        loginService.doLogin("verysafepass")
        waitForWorkToComplete()
        verify(encryptionService).validateInputAgainstKey(any())
        assert(loginService.loginState.value == LoginStateEnum.PASSWORD_ACCEPTED)
    }

    @Test
    fun `Test verify password integrity with empty pass`() = runBlocking {
        val returnType = loginService.verifyPasswordIntegrity("")
        waitForWorkToComplete()
        assert(!returnType)
        assert(loginService.loginState.value == LoginStateEnum.ERROR_NO_PASSWORD)
    }

    @Test
    fun `Test verify password integrity with small pass`() = runBlocking {
        val returnType = loginService.verifyPasswordIntegrity("123")
        waitForWorkToComplete()
        assert(!returnType)
        assert(loginService.loginState.value == LoginStateEnum.ERROR_TOO_SHORT_PASS)
    }


    @Test
    fun `Test verify password integrity with good pass`() = runBlocking {
        val returnType = loginService.verifyPasswordIntegrity("verysafepass")
        waitForWorkToComplete()
        assert(returnType)
        assert(loginService.loginState.value == LoginStateEnum.DEFAULT)
    }

    @Test
    fun `Test clear login data`() = runBlocking {
        loginService.clearLoginState()
        waitForWorkToComplete()
        assert(loginService.loginState.value == LoginStateEnum.DEFAULT)
    }


    /**
     * The suspending coroutine can cause the test to fail if its run in bulk
     * Introducing a minor delay sorts this out
     * */
    private suspend fun waitForWorkToComplete(timeout: Long = 10) {
        delay(timeout)
    }
}