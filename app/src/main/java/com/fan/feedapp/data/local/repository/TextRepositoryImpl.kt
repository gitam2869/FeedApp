package com.fan.feedapp.data.local.repository

import com.fan.feedapp.data.local.dao.TextDao
import com.fan.feedapp.data.local.model.TextData
import com.fan.feedapp.domain.repository.TextRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TextRepositoryImpl @Inject constructor(private val textDao: TextDao) :
    TextRepository {
    override suspend fun addText(textData: TextData) {
        withContext(Dispatchers.IO) {
            textDao.add(textData)
        }
    }

    override suspend fun getTextList() = withContext(Dispatchers.IO) {
        return@withContext textDao.getTexts()
    }

}