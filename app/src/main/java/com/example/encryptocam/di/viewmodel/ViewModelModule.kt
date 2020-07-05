package com.example.encryptocam.di.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.encryptocam.domain.files.FilesService
import com.example.encryptocam.domain.login.LoginService
import com.example.encryptocam.screens.camera.CameraViewModel
import com.example.encryptocam.screens.gallery.GalleryViewModel
import com.example.encryptocam.screens.login.LoginViewModel
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider
import kotlin.reflect.KClass

@Module
class ViewModelModule {
    @MustBeDocumented
    @kotlin.annotation.Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    @MapKey
    internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

    @Provides
    fun viewModelFactory(providerMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelFactory {
        return ViewModelFactory(providerMap)
    }

    @Provides
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    fun galleryViewModel(context: Context, fileService: FilesService): ViewModel {
        return GalleryViewModel(context, fileService)
    }

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModel(loginService: LoginService): ViewModel {
        return LoginViewModel(loginService)
    }

    @Provides
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    fun cameraViewModel(context: Context, fileService: FilesService): ViewModel {
        return CameraViewModel(context, fileService)
    }
}