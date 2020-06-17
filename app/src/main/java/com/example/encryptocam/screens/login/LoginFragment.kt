package com.example.encryptocam.screens.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login, LoginViewModel::class) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginStateEnum.observe(viewLifecycleOwner, Observer {
            text_login_layout.error = when (it) {
                LoginStateEnum.ERROR_TOO_SHORT_PASS -> getString(R.string.error_login_too_short_password)
                LoginStateEnum.ERROR_INVALID_PASS -> getString(R.string.error_login_invalid_password)
                LoginStateEnum.ERROR_NO_PASSWORD -> getString(R.string.error_login_no_password_provided)
                else -> null
            }
        })
        text_login.afterTextChanged {
            viewModel.checkLoginTextIntegrity(it)
        }
    }
}