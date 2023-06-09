package hello.yunho.domain.usecase

import com.aallam.openai.api.BetaOpenAI
import hello.yunho.domain.repository.ChatGPTRepository
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
class SendChatUseCase @Inject constructor(private val chatGPTRepository: ChatGPTRepository) {
    suspend fun invoke(chat:String) = chatGPTRepository.sendChat(chat)
}