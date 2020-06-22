package com.example.encryptocam.screens.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.databinding.FragmentLoginBinding
import com.example.encryptocam.domain.login.LoginStateEnum
import com.example.encryptocam.navigation.NavCommand
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login, LoginViewModel::class) {
    @SuppressLint("RestrictedApi")
    private fun showToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setHomeButtonEnabled(false)
            setDisplayHomeAsUpEnabled(false)
            setDefaultDisplayHomeAsUpEnabled(false) //some phone models require this
        }
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginState.observe(viewLifecycleOwner, Observer {
            text_login_layout.error = when (it) {
                LoginStateEnum.ERROR_TOO_SHORT_PASS -> getString(R.string.error_login_too_short_password)
                LoginStateEnum.ERROR_INVALID_PASS -> getString(R.string.error_login_invalid_password)
                LoginStateEnum.ERROR_NO_PASSWORD -> getString(R.string.error_login_no_password_provided)
                LoginStateEnum.PASSWORD_ACCEPTED -> {
                    viewModel.clearLoginState()
                    viewModel.navigate(NavCommand.CameraFragment)
                    null
                }
                else -> null
            }
        })
        text_login.afterTextChanged {
            GlobalScope.launch {
                viewModel.checkLoginTextIntegrity(it)
            }
        }
    }
}