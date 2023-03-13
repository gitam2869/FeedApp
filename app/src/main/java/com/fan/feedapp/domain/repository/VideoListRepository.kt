package com.fan.feedapp.domain.repository

import com.fan.feedapp.data.remote.model.VideoDTO

interface VideoListRepository {

    suspend fun getVideoList(): VideoDTO

}