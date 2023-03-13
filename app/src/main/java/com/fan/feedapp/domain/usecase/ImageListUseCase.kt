package com.fan.feedapp.domain.usecase

import com.fan.feedapp.data.userstorage.model.toDomainImage
import com.fan.feedapp.domain.model.Image
import com.fan.feedapp.domain.repository.ImageListRepository
import com.fan.feedapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ImageListUseCase @Inject constructor(private val repository: ImageListRepository) {

    operator fun invoke(start: Int, end: Int): Flow<Resource<List<Image>>> = flow {
        try {
            emit(Resource.Loading())

            val response = repository.getImageList(start, end)
            val list =
                if (response.isNullOrEmpty() || response.isEmpty()) emptyList<Image>() else response.map { it.toDomainImage() }
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