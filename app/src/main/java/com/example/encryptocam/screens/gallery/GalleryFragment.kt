package com.example.encryptocam.screens.gallery

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.encryptocam.R
import com.example.encryptocam.commons.base.adapters.ImageAdapter
import com.example.encryptocam.commons.base.adapters.ImageAdapter.OnBaseAdapterClickListener
import com.example.encryptocam.commons.base.fragment.BaseFragment
import com.example.encryptocam.databinding.FragmentGalleryBinding
import com.example.encryptocam.navigation.NavCommand
import org.koin.android.ext.android.get
import java.io.File

class GalleryFragment : BaseFragment<GalleryViewModel, FragmentGalleryBinding>(R.layout.fragment_gallery, GalleryViewModel::class) {
    private val adapter: ImageAdapter<File> = ImageAdapter(R.layout.adapter_item_picture, OnBaseAdapterClickListener { viewModel.onPictureClicked(it) }, get())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.galleryPictures.observe(viewLifecycleOwner, Observer { adapter.items = it })
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.rvPictureGrid.layoutManager = layoutManager
        binding.rvPictureGrid.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        showToolbar()
    }

    private fun showToolbar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            show()
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDefaultDisplayHomeAsUpEnabled(true)
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.navigate(NavCommand.CameraFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}