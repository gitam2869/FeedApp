package com.fan.feedapp.domain.usecase

import com.fan.feedapp.data.remote.model.toDomainVideo
import com.fan.feedapp.domain.model.Video
import com.fan.feedapp.domain.repository.VideoListRepository
import com.fan.feedapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class VideoListUseCase @Inject constructor(private val repository: VideoListRepository) {

    operator fun invoke(): Flow<Resource<List<Video>>> = flow {
        try {
            emit(Resource.Loading())

            val response = repository.getVideoList()
            val list =
                if (response.isNullOrEmpty() || response.isEmpty()) emptyList<Video>() else response.map { it.toDomainVideo() }
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