package com.example.domain.usecase

import com.aallam.openai.api.BetaOpenAI
import com.example.domain.repository.ChatGPTRepository
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
class SendChatNoFlowUseCase  @Inject constructor(private val chatGPTRepository: ChatGPTRepository) {

    suspend fun getChat(chat: String) = chatGPTRepository.sendChatNoFlow(chat)
}