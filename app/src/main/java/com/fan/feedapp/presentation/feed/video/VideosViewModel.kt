package com.fan.feedapp.presentation.feed.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fan.feedapp.domain.usecase.VideoListUseCase
import com.fan.feedapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VideosViewModel @Inject constructor(private val videoListUseCase: VideoListUseCase) :
    ViewModel() {

    private val _videoList = MutableStateFlow(VideoListState())
    val videoList: StateFlow<VideoListState> = _videoList

    fun getVideoList() {
        videoListUseCase.invoke().onEach {
            when (it) {
                is Resource.Loading -> {
                    _videoList.value = VideoListState(isLoading = true)
                }
                is Resource.Error -> {
                    _videoList.value = VideoListState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _videoList.value = VideoListState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}