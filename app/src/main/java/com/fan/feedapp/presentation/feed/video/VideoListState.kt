package com.fan.feedapp.presentation.feed.video

import com.fan.feedapp.domain.model.Video

data class VideoListState(
    val data: List<Video>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)