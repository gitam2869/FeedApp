package com.fan.feedapp.presentation.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fan.feedapp.R
import com.fan.feedapp.domain.model.Image
import com.fan.feedapp.domain.model.Text
import com.fan.feedapp.domain.model.Video
import com.fan.feedapp.presentation.feed.data.Feed
import com.fan.feedapp.presentation.feed.viewholder.ImageViewHolder
import com.fan.feedapp.presentation.feed.viewholder.LoadingViewHolder
import com.fan.feedapp.presentation.feed.viewholder.TextViewHolder
import com.fan.feedapp.presentation.feed.viewholder.VideoViewHolder

class FeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Feed>() {
        override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean {
            return newItem === oldItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Feed.TYPE_VIDEO -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
                VideoViewHolder(view)
            }
            Feed.TYPE_IMAGE -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
                ImageViewHolder(view)
            }
            Feed.TYPE_TEXT -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
                TextViewHolder(view)
            }
            Feed.TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_loading, parent, false)
                LoadingViewHolder(view)
            }
            else -> {
                throw java.lang.IllegalArgumentException()
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        when (differ.currentList[position].type) {
            Feed.TYPE_VIDEO -> {
                val videoViewHolder = holder as VideoViewHolder
                videoViewHolder.bind(videoViewHolder, currentItem as Video)
            }
            Feed.TYPE_IMAGE -> {
                val imageViewHolder = holder as ImageViewHolder
                imageViewHolder.bind(imageViewHolder, currentItem as Image)
            }
            Feed.TYPE_TEXT -> {
                val textViewHolder = holder as TextViewHolder
                textViewHolder.bind(textViewHolder, currentItem as Text)
            }
            Feed.TYPE_LOADING -> {
                val loadingViewHolder = holder as LoadingViewHolder
                loadingViewHolder.bind(loadingViewHolder)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position].type) {
            Feed.TYPE_VIDEO -> {
                Feed.TYPE_VIDEO
            }
            Feed.TYPE_IMAGE -> {
                Feed.TYPE_IMAGE
            }
            Feed.TYPE_TEXT -> {
                Feed.TYPE_TEXT
            }
            else -> {
                Feed.TYPE_LOADING
            }
        }
    }

    fun submitList(list: List<Feed>) {
        differ.submitList(list)
    }
}