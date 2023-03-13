package com.fan.feedapp.domain.usecase

import com.fan.feedapp.data.local.model.TextData
import com.fan.feedapp.domain.repository.TextRepository
import com.fan.feedapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TextAddUseCase @Inject constructor(private val repository: TextRepository) {

    operator fun invoke(textData: TextData): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = repository.addText(textData)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error!"))
        }
    }

}