package com.fan.feedapp.domain.model

import com.fan.feedapp.presentation.feed.data.Feed

data class Video(
    override val type: Int,
    val id: String,
    val title: String,
    val thumbnail: String,
    val url: String?,
    val views: Long,
    val releaseDate: String,
    val duration: String
) : Feed()
