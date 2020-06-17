package com.example.encryptocam.screens.launcher

import android.Manifest
import android.os.Bundle
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.activity.BaseActivity
import com.example.encryptocam.screens.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_launcher.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class LauncherActivity : BaseActivity(R.layout.activity_launcher) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startMainActivityWithPermissionCheck()
    }

    @NeedsPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun startMainActivity() {
        MainActivity.startThisActivity(applicationContext)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnPermissionDenied(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun handlePermissionDenied() {
        Snackbar.make(logo, getString(R.string.launcher_permissions_denied), Snackbar.LENGTH_INDEFINITE).setAction(R.string.general_ok) {
            startMainActivityWithPermissionCheck()
        }.show()
    }

    @OnNeverAskAgain(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun handleNeverAskAgain() {
        Snackbar.make(logo, getString(R.string.launcher_never_ask_again), Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.general_ok) { finish() }
            .show()
    }
}