package com.example.encryptocam.screens.camera

/**
 * Sealed classes used in conjunction with livedata provide a good abstraction on state management.
 * */
sealed class CameraState {
    object Default : CameraState()
    object TakePicture : CameraState()
    data class SetThumbnail(val thumbnail: ByteArray) : CameraState()
}