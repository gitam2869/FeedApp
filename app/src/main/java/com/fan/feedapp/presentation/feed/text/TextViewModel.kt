package com.fan.feedapp.presentation.feed.text

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fan.feedapp.data.local.model.TextData
import com.fan.feedapp.domain.usecase.TextAddUseCase
import com.fan.feedapp.domain.usecase.TextListUseCase
import com.fan.feedapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TextViewModel @Inject constructor(
    private val textListUseCase: TextListUseCase,
    private val textAddUseCase: TextAddUseCase
) :
    ViewModel() {

    private val _textList = MutableStateFlow(TextListState())
    val textList: StateFlow<TextListState> = _textList

    fun getTextList() {
        textListUseCase.invoke().onEach {
            when (it) {
                is Resource.Loading -> {
                    _textList.value = TextListState(isLoading = true)
                }
                is Resource.Error -> {
                    _textList.value = TextListState(error = it.message ?: "")
                }
                is Resource.Success -> {
                    _textList.value = TextListState(data = it.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addText(textData: TextData) {
        textAddUseCase.invoke(textData).onEach { }.launchIn(viewModelScope)
    }
}