package com.fan.feedapp.presentation.feed.data

abstract class Feed {
    abstract val type: Int

    companion object {
        const val TYPE_VIDEO = 0
        const val TYPE_IMAGE = 1
        const val TYPE_TEXT = 2
        const val TYPE_LOADING = 3
    }
}