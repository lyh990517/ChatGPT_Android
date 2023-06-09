package hello.yunho.chatgpt.datasource

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

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
}