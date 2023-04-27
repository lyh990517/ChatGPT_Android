package hello.yunho.domain.usecase

import com.aallam.openai.api.BetaOpenAI
import hello.yunho.domain.repository.ChatGPTRepository
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
class MakeVariationImageUseCase @Inject constructor(private val chatGPTRepository: ChatGPTRepository) {
    suspend fun invoke(fileSource: String, numberOfImage: Int) =
        chatGPTRepository.requestVariationImage(fileSource, numberOfImage)
}