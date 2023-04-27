package hello.yunho.domain.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.image.ImageURL
import kotlinx.coroutines.flow.Flow

@OptIn(BetaOpenAI::class)
interface ChatGPTRepository {
    suspend fun sendChat(chat: String): Flow<ChatCompletionChunk>

    suspend fun requestCreateImage(prompt: String, numberOfImage: Int): List<ImageURL>
}