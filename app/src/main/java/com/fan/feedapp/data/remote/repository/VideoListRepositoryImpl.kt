package com.fan.feedapp.data.remote.repository

import com.fan.feedapp.data.remote.api.FeedAPI
import com.fan.feedapp.data.remote.model.VideoDTO
import com.fan.feedapp.domain.repository.VideoListRepository

class VideoListRepositoryImpl(private val feedAPI: FeedAPI) : VideoListRepository {

    override suspend fun getVideoList(): VideoDTO {
        return feedAPI.getVideoList()
    }

}