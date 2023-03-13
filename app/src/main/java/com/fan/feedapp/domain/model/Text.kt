package com.fan.feedapp.domain.model

import com.fan.feedapp.presentation.feed.data.Feed

data class Text(
    override val type: Int,
    val id: Int,
    val text: String,
) : Feed()
