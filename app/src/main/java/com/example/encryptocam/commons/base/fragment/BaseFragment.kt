package com.example.encryptocam.commons.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.encryptocam.application.EncryptoCamApplication
import com.example.encryptocam.commons.base.viewmodel.BaseFragmentViewModel
import com.example.encryptocam.di.application.EncryptCamAppComponent
import com.example.encryptocam.di.presentation.PresentationComponent
import com.example.encryptocam.di.presentation.PresentationModule
import com.example.encryptocam.di.viewmodel.ViewModelFactory
import com.example.encryptocam.navigation.NavCommand
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseFragment<vm : BaseFragmentViewModel, B : ViewDataBinding>(@LayoutRes layout: Int, private val vmClass: KClass<vm>) : Fragment(layout) {
    private val navObserver: Observer<NavCommand> = Observer { onNavigationCommand(it) }

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    lateinit var viewModel: vm

    open val binding: B by bind() //bind the viewmodel to the fragment by settings its viewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
    }

    abstract fun onAttachInject()

    override fun onAttach(context: Context) {
        onAttachInject()
        viewModel = ViewModelProviders.of(this, mViewModelFactory).get(vmClass.javaObjectType)
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

    private var mIsInjectorUsed = false

    @get:UiThread
    protected val presentationComponent: PresentationComponent
        protected get() {
            if (mIsInjectorUsed) {
                throw RuntimeException("there is no need to use injector more than once")
            }
            mIsInjectorUsed = true
            return applicationComponent.newPresentationComponent(PresentationModule(requireActivity()))
        }

    private val applicationComponent: EncryptCamAppComponent
        private get() = (requireActivity().application as EncryptoCamApplication).getEncryptCamAppComponent()
}