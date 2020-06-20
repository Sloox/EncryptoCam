package com.example.encryptocam.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.domain.login.LoginStateEnum
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepo: LoginService) : BaseFragmentViewModel() {
    val loginPassword = MutableLiveData<String>()
    val loginState: LiveData<LoginStateEnum> = loginRepo.loginState

    fun attemptLogin(loginPassword: String?) = GlobalScope.launch {
        if (checkLoginTextIntegrity(loginPassword)) {
            loginRepo.doLogin(loginPassword!!)
        }
    }

    suspend fun checkLoginTextIntegrity(loginPassword: String?): Boolean {
        return loginRepo.verifyPasswordIntegrity(loginPassword)
    }

    fun clearLoginState() = GlobalScope.launch {
        loginRepo.doLogin("")
    }

}