package com.example.encryptocam.commons.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.navigation.NavCommand
import kotlin.reflect.KClass
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment<vm : BaseFragmentViewModel, B : ViewDataBinding>(@LayoutRes layout: Int, vmClass: KClass<vm>) : Fragment(layout) {
    private val navObserver: Observer<NavCommand> = Observer { onNavigationCommand(it) }

    val viewModel: vm by viewModel(vmClass)

    open val binding: B by bind() //bind the viewmodel to the fragment by settings its viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.navCommand.observe(this, navObserver)
        viewModel.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        viewModel.navCommand.removeObserver(navObserver)
        viewModel.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this //update the ui when changes occur & lazy instantiate the binding value
    }

    open fun onNavigationCommand(command: NavCommand) {
        when (command) {
            is NavCommand.Back -> findNavController().navigateUp()
            else -> findNavController().navigate(command.cmdID)
        }
    }
}