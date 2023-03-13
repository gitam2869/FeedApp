package com.fan.feedapp.domain.repository

import com.fan.feedapp.data.local.model.TextData

interface TextRepository {

    suspend fun addText(textData: TextData)
    suspend fun getTextList(): List<TextData>
}