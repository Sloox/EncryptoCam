package com.example.encryptocam.di

import com.example.encryptocam.screens.camera.CameraFragment
import com.example.encryptocam.screens.camera.CameraViewModel
import com.example.encryptocam.screens.gallery.GalleryFragment
import com.example.encryptocam.screens.gallery.GalleryViewModel
import com.example.encryptocam.screens.login.LoginFragment
import com.example.encryptocam.screens.login.LoginViewModel
import org.koin.android.experimental.dsl.viewModel
import org.koin.androidx.fragment.dsl.fragment
import org.koin.dsl.module

/**
 *  Koins representation of modules that are to be injected.
 *  uiModules contains all ui (fragments) and view(viewmodel) related injectables
 * */
val uiModules = module {
    viewModel<CameraViewModel>()
    viewModel<LoginViewModel>()
    viewModel<GalleryViewModel>()

    fragment { GalleryFragment() }
    fragment { CameraFragment() }
    fragment { LoginFragment() }
}

