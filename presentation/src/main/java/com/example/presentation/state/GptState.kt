package com.example.presentation.state

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionChunk
import kotlinx.coroutines.flow.Flow

@OptIn(BetaOpenAI::class)
sealed class GptState{
    object Loading: GptState()

    data class Success(val chatData: Flow<ChatCompletionChunk>): GptState()

    data class SuccessNoFlow(val chatData: ChatCompletion): GptState()

    data class Error(val t : Throwable): GptState()

    object End: GptState()
}
