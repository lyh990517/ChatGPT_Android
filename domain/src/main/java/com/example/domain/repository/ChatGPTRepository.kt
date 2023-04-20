package com.example.domain.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionChunk
import kotlinx.coroutines.flow.Flow

@OptIn(BetaOpenAI::class)
interface ChatGPTRepository {
    suspend fun sendChat(chat:String): Flow<ChatCompletionChunk>

    suspend fun sendChatNoFlow(chat:String): ChatCompletion
}