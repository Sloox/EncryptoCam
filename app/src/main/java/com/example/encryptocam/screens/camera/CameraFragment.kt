package com.example.encryptocam.screens.camera

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.commons.base.fragment.roUI
import com.example.encryptocam.commons.extensions.loge
import com.example.encryptocam.commons.extensions.logi
import com.example.encryptocam.databinding.FragmentCameraBinding
import com.example.encryptocam.utils.CameraUtils
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.include_picture_controls.*
import kotlinx.android.synthetic.main.include_top_action_bar_camera.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * CameraX is used within this CameraFragment implementation.
 * CameraX is a relatively new jetpack library that abstracts as much as possible the camera2 implementation of android away from the developer.
 * If there is interest in camera2 implementations i have worked with please the following two github libraries
 * KamUtils:https://github.com/Sloox/KamUtils
 * Kamscii:https://github.com/Sloox/Camscii
 * */
class CameraFragment : BaseFragment<CameraViewModel, FragmentCameraBinding>(R.layout.fragment_camera, CameraViewModel::class) {
    private lateinit var viewFinder: PreviewView
    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

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
            cameraExecutor = Executors.newSingleThreadExecutor()
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
        // ImageCapture
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        cameraProvider?.unbindAll()
        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        preview?.setSurfaceProvider(viewFinder.createSurfaceProvider())
    }

    private fun initCameraUI() {
        viewModel.aemEnabled.observe(viewLifecycleOwner, Observer { if (it != null) changeFocusAndMetering(it) })
        viewModel.flashStateEnabled.observe(viewLifecycleOwner, Observer { if (it != null) enableFlash(it) })
        viewModel.frontFacingCameraSelection.observe(viewLifecycleOwner, Observer { if (it != null) changeCameraFacing(it) })
        viewModel.camCommandState.observe(viewLifecycleOwner, Observer { if (it != null) processCamCommands(it) })
    }

    private fun processCamCommands(commands: CameraState) = when (commands) {
        is CameraState.TakePicture -> takePicture()
        is CameraState.SetThumbnail -> setThumbNailImage(commands.thumbnail)
        is CameraState.Default -> {
            //left blank intentionally
        }
    }

    private fun takePicture() {
        imageCapture?.let {
            val bufferedOutputStream = ByteArrayOutputStream()
            val outputOptions = ImageCapture.OutputFileOptions.Builder(bufferedOutputStream).build()
            it.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    logi("Image written to buffer")
                    GlobalScope.launch {
                        viewModel.encryptAndSaveImage(bufferedOutputStream)
                        setThumbNailImage(bufferedOutputStream)
                    }
                }

                override fun onError(exception: ImageCaptureException) = roUI {
                    loge("Failed to capture image: $exception")
                    Toast.makeText(requireContext(), getString(R.string.error_camera_falied_to_take_picture), Toast.LENGTH_LONG).show()
                    viewModel.clearCameraState()
                }
            })
        }
    }

    private fun setThumbNailImage(picStream: ByteArrayOutputStream) {
        setThumbNailImage(picStream.toByteArray())
    }

    private fun setThumbNailImage(picStream: ByteArray) = roUI {
        val paddingSize = resources.getDimension(R.dimen.stroke_small).toInt()
        button_gallery.setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
        Glide.with(button_gallery)
            .load(picStream)
            .apply(RequestOptions.circleCropTransform())
            .into(button_gallery)
    }

    /*Camera Property helpers*/
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

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
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

    private fun hideSystemUI() = (requireActivity() as AppCompatActivity).supportActionBar?.hide()

}