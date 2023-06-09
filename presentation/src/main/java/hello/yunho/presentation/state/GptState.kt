package hello.yunho.presentation.state

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionChunk

@OptIn(BetaOpenAI::class)
sealed class GptState {
    object Idle : GptState()

    object Loading : GptState()

    object LoadChat : GptState()

    data class Error(val t: Throwable) : GptState()

    data class End(val data: ChatCompletionChunk) : GptState()
}
