package com.example.encryptocam.commons.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.encryptocam.BR
import com.example.encryptocam.commons.base.viewmodel.BaseActivityViewModel
import org.koin.androidx.fragment.android.setupKoinFragmentFactory
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass


abstract class BaseMvvmActivity<vm : BaseActivityViewModel, B : ViewDataBinding>(@LayoutRes val layout: Int, vmClass: KClass<vm>) : AppCompatActivity() {
    val viewModel: vm by viewModel(vmClass)
    lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, layout, null, false)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.setViewModel(viewModel)
        setupKoinFragmentFactory()
    }
}

fun ViewDataBinding.setViewModel(vm: BaseActivityViewModel) = apply {
    setVariable(BR.viewModel, vm)
    executePendingBindings()
}