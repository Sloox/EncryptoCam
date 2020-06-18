package com.example.encryptocam.domain.login

import androidx.lifecycle.LiveData


interface LoginRepository {
    val loginState: LiveData<LoginStateEnum>

    suspend fun doLogin(password: String)
    suspend fun verifyPasswordIntegrity(password: String?): Boolean

    suspend fun clearLoginState()

}