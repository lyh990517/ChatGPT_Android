package com.example.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.example.domain.usecase.SendChatUseCase
import com.example.presentation.model.ChatUiModel
import com.example.presentation.state.GptState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ChatGPTViewModel @Inject constructor(private val sendChatUseCase: SendChatUseCase) :
    ViewModel() {
    private val _gptState = MutableStateFlow<GptState>(GptState.Loading)
    val gptState = _gptState

    val input = mutableStateOf("")
    val chatResult = MutableStateFlow("")

    val onSend: (String) -> Unit = {
        input.value = ""
        chatList.add(ChatUiModel(it,isUser = true))
        _gptState.value = GptState.LoadChat
        sendChat(it)
    }
    val inputChange: (String) -> Unit = {
        input.value = it
    }
    val chatList = mutableStateListOf<ChatUiModel>()

    val onSubmit: (String) -> Unit = { chat ->
        chatList.add(ChatUiModel(chat = chat))
        chatResult.value = ""
    }

    private fun sendChat(chat: String) = viewModelScope.launch {
        sendChatUseCase.invoke(chat).catch {
            Log.e("error", "${it.message}")
            chatResult.value += it.message
            _gptState.value = GptState.Error(it)
        }.collect {
            chatResult.value += it.choices[0].delta?.content ?: ""
            if (it.choices[0].finishReason == "stop") {
                _gptState.value = GptState.End(it)
                onSubmit(chatResult.value)
                return@collect
            }
            _gptState.value = GptState.LoadChat
        }
    }

}