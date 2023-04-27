package hello.yunho.presentation.state

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk

@OptIn(BetaOpenAI::class)
sealed class ChatState {
    object Idle : ChatState()

    object Loading : ChatState()

    object LoadChat : ChatState()

    data class Error(val t: Throwable) : ChatState()

    data class End(val data: ChatCompletionChunk) : ChatState()
}
