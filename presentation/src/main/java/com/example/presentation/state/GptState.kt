package com.example.presentation.state

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import kotlinx.coroutines.flow.Flow

@OptIn(BetaOpenAI::class)
sealed class GptState {
    object Loading : GptState()

    data class LoadChat(val data: ChatCompletionChunk) : GptState()

    data class Error(val t: Throwable) : GptState()

    object End : GptState()
}
