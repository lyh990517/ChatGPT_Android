package com.example.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.example.domain.usecase.SendChatNoFlowUseCase
import com.example.domain.usecase.SendChatUseCase
import com.example.presentation.state.GptState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ChatGPTViewModel @Inject constructor(
    private val sendChatUseCase: SendChatUseCase,
    private val sendChatNoFlowUseCase: SendChatNoFlowUseCase
    ) :
    ViewModel() {
    private val _gptSate = MutableStateFlow<GptState>(GptState.Loading)
    val gptState = _gptSate
    fun sendChat(chat: String) = viewModelScope.launch {
        val flow = flow {
            sendChatUseCase.getFlow(chat).catch {
                gptState.value = GptState.Error(it)
            }.collect {
                emit(it)
            }
        }
        gptState.value = GptState.Success(flow)
    }

    fun sendChatNoFlow(chat: String) = viewModelScope.launch {
        gptState.value = GptState.SuccessNoFlow(sendChatNoFlowUseCase.getChat(chat))
    }
}