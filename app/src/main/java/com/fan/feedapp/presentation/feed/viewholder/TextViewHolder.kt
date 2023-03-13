package com.fan.feedapp.presentation.feed.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fan.feedapp.databinding.ItemTextBinding
import com.fan.feedapp.domain.model.Text

class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemTextBinding.bind(itemView)

    fun bind(holder: RecyclerView.ViewHolder, text: Text) {
        binding.tvText.text = text.text
    }
}