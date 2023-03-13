package com.fan.feedapp.presentation.feed.text

import com.fan.feedapp.domain.model.Text

data class TextListState(
    val data: List<Text>? = null,
    val error: String = "",
    val isLoading: Boolean = false
)