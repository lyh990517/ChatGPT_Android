package com.example.chatgpt.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.example.chatgpt.datasource.GPTDataSource
import com.example.domain.repository.ChatGPTRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@BetaOpenAI
class ChatGPTRepositoryImpl@Inject constructor(private val gptDataSource: GPTDataSource) : ChatGPTRepository {
    override suspend fun sendChat(chat: String): Flow<ChatCompletionChunk> = gptDataSource.sendChat(chat)
}