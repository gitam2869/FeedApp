package com.fan.feedapp.data.remote.model

import com.fan.feedapp.domain.model.Video
import com.fan.feedapp.presentation.feed.data.Feed

data class VideoItem(
    val id: String,
    val author: String?,
    val description: String?,
    val duration: String?,
    val isLive: Boolean,
    val subscriber: String?,
    val thumbnailUrl: String?,
    val title: String?,
    val uploadTime: String?,
    val videoUrl: String?,
    val views: String?
)

fun VideoItem.toDomainVideo(): Video {
    return Video(
        type = Feed.TYPE_VIDEO,
        id = this.id,
        title = this.title ?: "",
        thumbnail = this.thumbnailUrl ?: "",
        url = this.videoUrl ?: "",
        views = this.views?.replace(",", "").toString().toLong(),
        releaseDate = this.uploadTime ?: "",
        duration = this.duration ?: ""
    )
}