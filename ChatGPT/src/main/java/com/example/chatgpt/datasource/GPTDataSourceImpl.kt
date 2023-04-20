package com.example.chatgpt.datasource

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.*
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.example.API_KEY
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(BetaOpenAI::class)
class GPTDataSourceImpl @Inject constructor(private val openAI: OpenAI) : GPTDataSource {
    override suspend fun sendChat(chat: String): Flow<ChatCompletionChunk> {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = chat
                )
            )
        )
        return openAI.chatCompletions(chatCompletionRequest)
    }

    override suspend fun sendChatNoFlow(chat: String): ChatCompletion {
        val chatCompletionRequest = ChatCompletionRequest(
            model = ModelId("gpt-3.5-turbo"),
            messages = listOf(
                ChatMessage(
                    role = ChatRole.User,
                    content = chat
                )
            )
        )
        return openAI.chatCompletion(chatCompletionRequest)
    }
}