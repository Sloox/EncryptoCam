package com.example.encryptocam.di.presentation

import com.example.encryptocam.di.viewmodel.ViewModelModule
import com.example.encryptocam.screens.MainActivity
import com.example.encryptocam.screens.camera.CameraFragment
import com.example.encryptocam.screens.gallery.GalleryFragment
import com.example.encryptocam.screens.login.LoginFragment
import dagger.Subcomponent

@Subcomponent(modules = [PresentationModule::class, ViewModelModule::class])
interface PresentationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(gallery: GalleryFragment)
    fun inject(camera: CameraFragment)
    fun inject(login: LoginFragment)
}