package hello.yunho.chatgpt.datasource

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.image.*
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.Source
import okio.source
import java.io.File
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

    override suspend fun requestCreateImage(
        prompt: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> {
        val image = openAI.imageURL(
            creation = ImageCreation(
                prompt = prompt,
                n = numberOfImage,
                size = ImageSize.is1024x1024
            )
        )
        return flow { emit(image) }
    }

    override suspend fun requestVariationImage(
        fileSource: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> {
        val file = File(fileSource)
        val images = openAI.imageURL( // or openAI.imageJSON
            variation = ImageVariation(
                image = FileSource(name = "file", source = file.source()),
                n = 1,
                size = ImageSize.is1024x1024
            )
        )
        return flow { emit(images) }
    }

    override suspend fun requestEditImage(
        prompt: String,
        fileSource: String,
        masked: String,
        numberOfImage: Int
    ): Flow<List<ImageURL>> {
        val file = File(fileSource)
        val mask = File(fileSource)

        val images = openAI.imageURL( // or openAI.imageJSON
            edit = ImageEdit(
                image = FileSource(name = "file", source = file.source()),
                mask = FileSource(name = "mask", source = mask.source()),
                prompt = prompt,
                n = 1,
                size = ImageSize.is1024x1024
            )
        )
        return flow { emit(images) }
    }
}