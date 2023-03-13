package com.fan.feedapp.presentation.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fan.feedapp.R
import com.fan.feedapp.databinding.ItemVideoBinding
import com.fan.feedapp.domain.model.Video
import kotlin.math.ln
import kotlin.math.pow

class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val binding = ItemVideoBinding.bind(itemView)

    init {
        itemView.tag = this
    }

    fun bind(holder: RecyclerView.ViewHolder, video: Video) {

        binding.run {
            tvTitle.text = video.title
            tvViews.text = getFormatedNumber(video.views)
            tvRelease.text = video.releaseDate
            tvDuration.text = video.duration
        }

        Glide.with(holder.itemView.getContext())
            .load(video.thumbnail)
            .placeholder(R.color.black)
            .transition(DrawableTransitionOptions.withCrossFade(500))
            .into(binding.ivThumbnail)
    }

    fun getFormatedNumber(count: Long): String {
        if (count < 1000) return "" + count
        val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
        return String.format("%.1f %c", count / 1000.0.pow(exp.toDouble()), "kMGTPE"[exp - 1])
    }
}