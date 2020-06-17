package com.example.encryptocam.screens.login

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.navigation.NavCommand
import java.util.*

class LoginViewModel : BaseFragmentViewModel() {
    val loginPassword = MutableLiveData<String>()

    private val _loginState = MutableLiveData(LoginStateEnum.DEFAULT)
    val loginStateEnum: LiveData<LoginStateEnum> = _loginState

    fun attemptLogin(loginPassword: String?) {
        //TODO Abstract this further?
        if (!checkLoginTextIntegrity(loginPassword)) return

        if (Arrays.equals(Base64.decode(NOT_A_PASSWORD, 0), loginPassword!!.toByteArray())) {
            navigate(NavCommand.CameraFragment)
        } else {
            _loginState.postValue(LoginStateEnum.ERROR_INVALID_PASS)
        }
    }

    fun checkLoginTextIntegrity(loginPassword: String?): Boolean {
        return when {
            loginPassword.isNullOrEmpty() -> {
                _loginState.postValue(LoginStateEnum.ERROR_NO_PASSWORD)
                false
            }
            loginPassword.length < 4 -> {
                _loginState.postValue(LoginStateEnum.ERROR_TOO_SHORT_PASS)
                false

            }
            else -> {
                _loginState.postValue(LoginStateEnum.DEFAULT)
                true //at this point the login text integrity is good but it does not mean its a valid password
            }
        }
    }

    companion object {
        private const val NOT_A_PASSWORD = "bXlzYWZlcGFzc3dvcmQ=" //mysafepassword
    }

}