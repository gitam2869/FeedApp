package com.fan.feedapp.domain.usecase

import com.fan.feedapp.data.local.model.TextData
import com.fan.feedapp.data.local.model.toDomainText
import com.fan.feedapp.domain.model.Text
import com.fan.feedapp.domain.repository.TextRepository
import com.fan.feedapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TextListUseCase @Inject constructor(private val repository: TextRepository) {

    operator fun invoke(): Flow<Resource<List<Text>>> = flow {
        try {
            emit(Resource.Loading())

            val response = repository.getTextList()
            val list =
                if (response.isNullOrEmpty() || response.isEmpty()) emptyList<Text>() else response.map { it.toDomainText() }
            emit(Resource.Success(list))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error!"))
        } catch (e: IOException) {
            emit(Resource.Error(e.localizedMessage ?: "Internet Error"))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error!"))
        }
    }

}