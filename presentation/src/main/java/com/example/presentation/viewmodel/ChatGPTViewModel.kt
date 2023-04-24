package com.example.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.example.domain.usecase.SendChatUseCase
import com.example.presentation.state.GptState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ChatGPTViewModel @Inject constructor(private val sendChatUseCase: SendChatUseCase) :
    ViewModel() {
    private val _gptSate = MutableStateFlow<GptState>(GptState.Loading)
    val gptState = _gptSate

    val input = mutableStateOf("")
    val chatResult = MutableStateFlow("")

    val onSend: (String) -> Unit = {
        input.value = ""
        sendChat(it)
    }
    val inputChange: (String) -> Unit = {
        input.value = it
    }
    val onReset: (GptState) -> Unit = {
        _gptSate.value = GptState.End
    }

    fun sendChat(chat: String) = viewModelScope.launch {
        val flow = flow {
            sendChatUseCase.invoke(chat).catch {
                Log.e("it","${it.message}")
                gptState.value = GptState.Error(it)
            }.collect {
                emit(it)
            }
        }
        flow.collect {
            chatResult.value += it.choices[0].delta?.content ?: ""
            gptState.value = GptState.LoadChat(it)
        }
    }
}