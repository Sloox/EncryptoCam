package com.example.encryptocam.commons.base.fragment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.BR

fun <B : ViewDataBinding> BaseFragment<*, B>.bind(): Lazy<B> = lazy {
    androidx.databinding.DataBindingUtil.bind<B>(view ?: throw Exception("View is null"))?.setViewModel(viewModel) as B
}

fun ViewDataBinding.setViewModel(vm: BaseFragmentViewModel) = apply {
    setVariable(BR.viewModel, vm)
    executePendingBindings()
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

fun Fragment.roUI(f: () -> Unit) {
    this.activity?.runOnUiThread(f)
}