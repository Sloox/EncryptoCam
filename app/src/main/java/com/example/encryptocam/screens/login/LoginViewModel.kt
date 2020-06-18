package com.example.encryptocam.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.domain.login.LoginRepository
import com.example.encryptocam.domain.login.LoginStateEnum
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.inject

class LoginViewModel : BaseFragmentViewModel() {
    private val loginRepo: LoginRepository by inject()

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