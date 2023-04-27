package hello.yunho.chatgpt.repository

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.image.ImageURL
import hello.yunho.chatgpt.datasource.GPTDataSource
import hello.yunho.domain.repository.ChatGPTRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@BetaOpenAI
class ChatGPTRepositoryImpl @Inject constructor(private val gptDataSource: GPTDataSource) :
    ChatGPTRepository {
    override suspend fun sendChat(chat: String): Flow<ChatCompletionChunk> =
        gptDataSource.sendChat(chat)

    override suspend fun requestCreateImage(
        prompt: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> =
        gptDataSource.requestCreateImage(prompt, numberOfImage)

    override suspend fun requestVariationImage(
        fileSource: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> = gptDataSource.requestVariationImage(fileSource, numberOfImage)

    override suspend fun requestEditImage(
        prompt: String,
        fileSource: String,
        masked: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> =
        gptDataSource.requestEditImage(prompt, fileSource, masked, numberOfImage)
}