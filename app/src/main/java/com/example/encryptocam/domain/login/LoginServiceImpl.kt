package com.example.encryptocam.domain.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.domain.encryption.EncryptionService

class LoginServiceImpl(private val encryptionService: EncryptionService) : LoginService {
    private val _loginState: MutableLiveData<LoginStateEnum> = MutableLiveData(LoginStateEnum.DEFAULT) //default state on creation
    override val loginState: LiveData<LoginStateEnum> = _loginState

    override suspend fun doLogin(password: String) {
        if (!verifyPasswordIntegrity(password)) return

        if (encryptionService.validateInputAgainstKey(password.toByteArray())) {
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
}