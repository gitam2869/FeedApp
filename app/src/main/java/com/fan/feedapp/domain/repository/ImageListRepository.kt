package com.fan.feedapp.domain.repository

import com.fan.feedapp.data.userstorage.model.ImageDTO

interface ImageListRepository {
    suspend fun getImageList(start: Int, end: Int): ImageDTO
}