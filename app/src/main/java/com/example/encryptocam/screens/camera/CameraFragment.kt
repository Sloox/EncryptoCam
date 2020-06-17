package com.example.encryptocam.screens.camera

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.commons.extensions.logi
import com.example.encryptocam.databinding.FragmentCameraBinding
import com.example.encryptocam.utils.CameraUtils
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : BaseFragment<CameraViewModel, FragmentCameraBinding>(R.layout.fragment_camera, CameraViewModel::class) {
    private lateinit var viewFinder: PreviewView

    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewFinder = view_finder
        //wait for views to be laid out
        viewFinder.post {
            displayId = viewFinder.display.displayId
            updateCameraUI()
            initCamera()
        }
    }

    private fun initCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            lensFacing = when {
                CameraUtils.hasBackCamera(cameraProvider) -> CameraSelector.LENS_FACING_BACK
                CameraUtils.hasFrontCamera(cameraProvider) -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }
            // TODO updateCameraSwitchButton(),

            // Build and bind the camera use cases
            bindCameraToLifeCycle()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraToLifeCycle() {
        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { viewFinder.display.getRealMetrics(it) }
        logi("Screen metrics: ${metrics.widthPixels} x ${metrics.heightPixels}")
        val screenAspectRatio = CameraUtils.aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = viewFinder.display.rotation
        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        preview = Preview.Builder().setTargetAspectRatio(screenAspectRatio).setTargetRotation(rotation).build()
        cameraProvider?.unbindAll()
        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview)
        preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
    }

    private fun updateCameraUI() {
        //TODO
    }

    private fun hideSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}