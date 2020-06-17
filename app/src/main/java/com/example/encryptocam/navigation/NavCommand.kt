package com.example.encryptocam.navigation

import androidx.annotation.IdRes
import com.example.encryptocam.R

/**
 * Class that handles navigation and abstracts its implementation away into the baseFragment & baseViewmodels
 * Can add direct navigation here by matching the navCommand cmdID to the itemID defined in the navigation.xml file
 * There are many ways to navigate via androidX navigation, this is just one way.
 * This eases up direct navigation via custom ui components
 * */
sealed class NavCommand(@IdRes val cmdID: Int) {
    object Back : NavCommand(-1)
    object LoginFragment : NavCommand(R.id.nav_login)
    object CameraFragment : NavCommand(R.id.nav_camera)
    object GalleryFragment : NavCommand(R.id.nav_gallery)
}