package com.example.encryptocam.commons.base.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.encryptocam.BR
import com.example.encryptocam.domain.files.FilesRepository
import kotlinx.android.synthetic.main.adapter_item_picture.view.*
import java.io.File

class ImageAdapter<T>(@LayoutRes val layout: Int, private val diff: DiffCallback<T>? = null, private val onClick: OnBaseAdapterClickListener<T>? = null, private val filesRepository: FilesRepository) :
    RecyclerView.Adapter<ImageAdapter<T>.ViewHolder>() {

    constructor(@LayoutRes layout: Int, onClick: OnBaseAdapterClickListener<T>? = null, filesRepository: FilesRepository) : this(layout, null, onClick, filesRepository)

    var items: List<T> = listOf()
        set(value) {
            if (diff != null) {
                diff.oldItems = field
                diff.newItems = value
                val result = DiffUtil.calculateDiff(diff)
                field = value
                result.dispatchUpdatesTo(this)
            } else {
                field = value
                notifyDataSetChanged()
            }
        }

    class OnBaseAdapterClickListener<T>(val clickListener: (result: T) -> Unit) {
        fun onClick(result: T) = clickListener(result)
    }

    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setImageFromFile(item: T?) {
            if (item != null && item is File) {
                val pic = filesRepository.getPicture(item)
                Glide.with(itemView.context)
                    .load(pic)
                    .apply(
                        RequestOptions
                            .skipMemoryCacheOf(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .into(itemView.imageView)
            }
        }

        var item: T? = null
            set(value) {
                field = value
                binding.setVariable(BR.result, value)
                binding.setVariable(BR.onClick, onClick)
                binding.executePendingBindings()
            }
    }

    abstract class DiffCallback<T> : DiffUtil.Callback() {
        abstract var oldItems: List<T>
        abstract var newItems: List<T>

        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), layout, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = items[position]
        holder.setImageFromFile(holder.item)
    }
}