package com.example.encryptocam.screens.camera

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.commons.extensions.logi
import com.example.encryptocam.databinding.FragmentCameraBinding
import com.example.encryptocam.utils.CameraUtils
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.include_top_action_bar_camera.*
import java.util.concurrent.TimeUnit

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
            initCamera()
            initCameraUI()
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

    private fun initCameraUI() {
        viewModel.aemEnabled.observe(viewLifecycleOwner, Observer { if (it != null) changeFocusAndMetering(it) })
        viewModel.flashStateEnabled.observe(viewLifecycleOwner, Observer { if (it != null) enableFlash(it) })
        viewModel.frontFacingCameraSelection.observe(viewLifecycleOwner, Observer { if (it != null) changeCameraFacing(it) })
    }

    private fun changeCameraFacing(front: Boolean) {
        if (front) {
            if (CameraUtils.hasFrontCamera(cameraProvider)) {
                lensFacing = CameraSelector.LENS_FACING_FRONT
                bindCameraToLifeCycle()
            } else {
                //no front lens, toggle it back to the original back
                viewModel.toggleCameraFacing()
            }
        } else {
            lensFacing = CameraSelector.LENS_FACING_BACK
            bindCameraToLifeCycle()
        }
    }

    private fun enableFlash(value: Boolean) {
        camera?.let {
            if (!it.cameraInfo.hasFlashUnit()) return@let
            if (flash_button.isSelected) {
                flash_button.isSelected = false
                it.cameraControl.enableTorch(false)
            } else {
                flash_button.isSelected = true
                it.cameraControl.enableTorch(true)
            }
        }
    }

    private fun changeFocusAndMetering(value: Boolean) {
        if (value) {
            val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(viewFinder.width.toFloat(), viewFinder.height.toFloat())
            val centerWidth = viewFinder.width.toFloat() / 2
            val centerHeight = viewFinder.height.toFloat() / 2
            val autoFocusPoint = factory.createPoint(centerWidth, centerHeight)
            camera?.cameraControl?.startFocusAndMetering(FocusMeteringAction.Builder(autoFocusPoint, FocusMeteringAction.FLAG_AF).apply {
                setAutoCancelDuration(1, TimeUnit.SECONDS)
            }.build())
        } else {
            camera?.cameraControl?.cancelFocusAndMetering()
        }
    }

    private fun hideSystemUI() {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}