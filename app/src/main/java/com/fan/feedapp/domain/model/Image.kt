package com.fan.feedapp.domain.model

import com.fan.feedapp.presentation.feed.data.Feed

data class Image(
    override val type: Int,
    val title: String,
    val url: String,
) : Feed()
