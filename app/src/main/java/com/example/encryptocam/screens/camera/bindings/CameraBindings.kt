package com.example.encryptocam.screens.camera.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("setSelectedState")
fun ImageView.setSelectedState(selected: Boolean) {
    isSelected = selected
}