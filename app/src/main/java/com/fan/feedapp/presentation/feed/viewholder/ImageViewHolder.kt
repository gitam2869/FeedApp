package com.fan.feedapp.presentation.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fan.feedapp.R
import com.fan.feedapp.databinding.ItemImageBinding
import com.fan.feedapp.domain.model.Image

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemImageBinding.bind(itemView)

    fun bind(holder: RecyclerView.ViewHolder, image: Image) {
        Glide.with(holder.itemView.context)
            .load(image.url)
            .placeholder(R.color.gray)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(binding.ivImage)
    }
}