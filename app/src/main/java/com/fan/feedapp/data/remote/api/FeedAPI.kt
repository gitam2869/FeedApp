package com.fan.feedapp.data.remote.api

import com.fan.feedapp.data.remote.model.VideoDTO
import retrofit2.http.GET

interface FeedAPI {

    @GET("/test.json")
    suspend fun getVideoList(): VideoDTO

}