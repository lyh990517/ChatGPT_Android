package hello.yunho.domain.usecase

import com.aallam.openai.api.BetaOpenAI
import hello.yunho.domain.repository.ChatGPTRepository
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
class EditImageUseCase @Inject constructor(private val chatGPTRepository: ChatGPTRepository) {
    suspend fun invoke(prompt: String, fileSource: String, masked: String, numberOfImage: Int) =
        chatGPTRepository.requestEditImage(prompt, fileSource, masked, numberOfImage)
}