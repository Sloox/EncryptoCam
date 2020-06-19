package com.example.encryptocam.screens.camera

sealed class CameraState {
    object Default : CameraState()
    object TakePicture : CameraState()
    data class SetThumbnail(val thumbnail: ByteArray) : CameraState()
}