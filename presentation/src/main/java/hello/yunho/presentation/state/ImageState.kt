package hello.yunho.presentation.state

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.image.ImageURL

sealed class ImageState {
    object Idle : ImageState()

    object Loading : ImageState()

    object Create : ImageState()

    object MakeVariation : ImageState()

    object Edit : ImageState()

    @OptIn(BetaOpenAI::class)
    data class ImageLoadSuccess(val images: List<ImageURL>) : ImageState()

    data class Error(val t: Throwable) : ImageState()
}