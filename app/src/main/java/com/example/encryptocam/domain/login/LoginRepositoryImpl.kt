package com.example.encryptocam.domain.login

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class LoginRepositoryImpl : LoginRepository {
    private val _loginState: MutableLiveData<LoginStateEnum> = MutableLiveData(LoginStateEnum.DEFAULT) //default state on creation
    override val loginState: LiveData<LoginStateEnum> = _loginState

    override suspend fun doLogin(password: String) {
        if (!verifyPasswordIntegrity(password)) return

        if (Arrays.equals(Base64.decode(NOT_A_PASSWORD, 0), password.toByteArray())) {
            _loginState.postValue(LoginStateEnum.PASSWORD_ACCEPTED)
        } else {
            _loginState.postValue(LoginStateEnum.ERROR_INVALID_PASS)
        }
    }

    override suspend fun verifyPasswordIntegrity(password: String?): Boolean {
        return when {
            password.isNullOrEmpty() -> {
                _loginState.postValue(LoginStateEnum.ERROR_NO_PASSWORD)
                false
            }
            password.length < 4 -> {
                _loginState.postValue(LoginStateEnum.ERROR_TOO_SHORT_PASS)
                false

            }
            else -> {
                _loginState.postValue(LoginStateEnum.DEFAULT)
                true //at this point the login text integrity is good but it does not mean its a valid password
            }
        }
    }

    override suspend fun clearLoginState() {
        _loginState.postValue(LoginStateEnum.DEFAULT)
    }

    companion object {
        private const val NOT_A_PASSWORD = "bXlzYWZlcGFzc3dvcmQ=" //mysafepassword
    }
}