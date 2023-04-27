package hello.yunho.domain.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.image.ImageURL
import kotlinx.coroutines.flow.Flow

@OptIn(BetaOpenAI::class)
interface ChatGPTRepository {
    suspend fun sendChat(chat: String): Flow<ChatCompletionChunk>

    suspend fun requestCreateImage(prompt: String, numberOfImage: Int): Flow<List<ImageURL>>

    suspend fun requestVariationImage(fileSource: String, numberOfImage: Int): Flow<List<ImageURL>>

    suspend fun requestEditImage(prompt: String, fileSource: String, masked: String, numberOfImage: Int): Flow<List<ImageURL>>
}