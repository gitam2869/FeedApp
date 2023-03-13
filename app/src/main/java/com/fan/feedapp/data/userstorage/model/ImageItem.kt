package com.fan.feedapp.data.userstorage.model

import com.fan.feedapp.domain.model.Image
import com.fan.feedapp.presentation.feed.data.Feed

data class ImageItem(
    val title: String,
    val url: String
)

fun ImageItem.toDomainImage(): Image{
    return Image(
        type = Feed.TYPE_IMAGE,
        title = this.title,
        url = this.url
    )
}