package com.example.encryptocam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginStateEnum
import com.example.encryptocam.screens.login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class LoginViewModelTest {

    @Mock
    lateinit var loginService: LoginService

    @Spy
    private val loginState: LiveData<LoginStateEnum> = MutableLiveData(LoginStateEnum.DEFAULT)

    lateinit var loginViewModel: LoginViewModel

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        `when`(loginService.loginState).thenReturn(loginState)
        this.loginViewModel = LoginViewModel(loginService)
    }

    @Test //simple delegation
    fun `test blank password integrity`() = runBlocking {
        loginViewModel.checkLoginTextIntegrity("")
        verify(loginService).verifyPasswordIntegrity("")
        return@runBlocking
    }

    @Test //simple delegation
    fun `test password integrity`() = runBlocking {
        loginViewModel.checkLoginTextIntegrity("123")
        verify(loginService).verifyPasswordIntegrity("123")
        return@runBlocking
    }

    @Test
    fun `test blank password for login`() = runBlocking {
        `when`(loginService.verifyPasswordIntegrity("")).thenReturn(false)
        loginViewModel.attemptLogin("")
        waitForWorkToComplete()
        verify(loginService).verifyPasswordIntegrity("")
        verify(loginService, never()).doLogin("")
        return@runBlocking
    }


    @Test
    fun `test small password for login`() = runBlocking {
        `when`(loginService.verifyPasswordIntegrity("123")).thenReturn(false)
        loginViewModel.attemptLogin("123")
        waitForWorkToComplete()
        verify(loginService).verifyPasswordIntegrity("123")
        verify(loginService, never()).doLogin("123")
        return@runBlocking
    }


    @Test
    fun `test suitable password for login`() = runBlocking {
        `when`(loginService.verifyPasswordIntegrity("abc123123")).thenReturn(true)
        loginViewModel.attemptLogin("abc123123")
        waitForWorkToComplete()
        verify(loginService).verifyPasswordIntegrity("abc123123")
        verify(loginService).doLogin("abc123123")
        return@runBlocking
    }

    /**
     * The suspending coroutine can cause the test to fail if its run in bulk
     * Introducing a minor delay sorts this out
     * */
    private suspend fun waitForWorkToComplete(timeout: Long = 10) {
        delay(timeout)
    }
}