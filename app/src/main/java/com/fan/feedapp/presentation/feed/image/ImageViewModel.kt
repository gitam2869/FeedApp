package com.fan.feedapp.presentation.feed.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fan.feedapp.domain.usecase.ImageListUseCase
import com.fan.feedapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val imageListUseCase: ImageListUseCase) :
    ViewModel() {

    private val _imageList = MutableStateFlow(ImageListState())
    val imageList: StateFlow<ImageListState> = _imageList

    fun getImageList(start: Int, end: Int) {
        imageListUseCase.invoke(start, end).onEach {
            when (it) {
                is Resource.Loading -> {
                    _imageList.value = ImageListState(isLoading = true)
                }
                is Resource.Error -> {
                    _imageList.value = ImageListState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _imageList.value = ImageListState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}