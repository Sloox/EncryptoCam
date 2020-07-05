package com.example.encryptocam.di.application

import com.example.encryptocam.di.presentation.PresentationComponent
import com.example.encryptocam.di.presentation.PresentationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [EncryptoCamModule::class])
interface EncryptCamAppComponent {
    fun newPresentationComponent(presentationModule: PresentationModule): PresentationComponent
}