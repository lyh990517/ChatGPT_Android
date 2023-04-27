package hello.yunho.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import hello.yunho.domain.usecase.SendChatUseCase
import hello.yunho.presentation.model.ChatUiModel
import hello.yunho.presentation.state.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ChatViewModel @Inject constructor(private val sendChatUseCase: SendChatUseCase) :
    ViewModel() {
    private val _chatState = MutableStateFlow<ChatState>(ChatState.Idle)
    val gptState = _chatState

    val input = mutableStateOf("")
    val chatResult = MutableStateFlow("")

    val onSend: (String) -> Unit = {
        input.value = ""
        chatList.add(ChatUiModel(it, isUser = true))
        chatResult.value = "Loading..."
        _chatState.value = ChatState.Loading
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
            _chatState.value = ChatState.Error(it)
        }.collect {
            if (_chatState.value == ChatState.Loading) chatResult.value = ""
            _chatState.value = ChatState.LoadChat
            chatResult.value += it.choices[0].delta?.content ?: ""
            if (it.choices[0].finishReason == "stop") {
                _chatState.value = ChatState.End(it)
                onSubmit(chatResult.value)
                return@collect
            }
        }
    }

}