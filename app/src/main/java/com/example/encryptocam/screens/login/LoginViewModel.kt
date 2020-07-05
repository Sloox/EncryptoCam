package com.example.encryptocam.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginStateEnum
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginService: LoginService) : BaseFragmentViewModel() {
    val loginPassword = MutableLiveData<String>()
    val loginState: LiveData<LoginStateEnum> = loginService.loginState

    fun attemptLogin(loginPassword: String?) = GlobalScope.launch {
        if (checkLoginTextIntegrity(loginPassword)) {
            loginService.doLogin(loginPassword!!)
        }
    }

    suspend fun checkLoginTextIntegrity(loginPassword: String?): Boolean {
        return loginService.verifyPasswordIntegrity(loginPassword)
    }

    fun clearLoginState() = GlobalScope.launch {
        loginService.doLogin("")
    }

}