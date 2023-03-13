package com.fan.feedapp.presentation.feed.image

import com.fan.feedapp.domain.model.Image

data class ImageListState(
    val data: List<Image>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)