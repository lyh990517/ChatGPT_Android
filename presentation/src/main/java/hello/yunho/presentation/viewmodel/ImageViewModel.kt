package hello.yunho.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import dagger.hilt.android.lifecycle.HiltViewModel
import hello.yunho.domain.usecase.CreateImageUseCase
import hello.yunho.presentation.state.ImageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ImageViewModel @Inject constructor(private val createImageUseCase: CreateImageUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow<ImageState>(ImageState.Idle)
    val uiState = _uiState

    val imageURL = mutableStateOf("")
    val input = mutableStateOf("")
    val onSend: (String) -> Unit = {
        createImage(input.value, 1)
        input.value = ""
    }
    val inputChange: (String) -> Unit = {
        input.value = it
    }

    private fun createImage(prompt: String, numberOfImage: Int) = viewModelScope.launch {
        createImageUseCase.invoke(prompt, numberOfImage).catch {
            Log.e("it", "${it.message}")
            _uiState.value = ImageState.Error(it)
        }.collect {
            _uiState.value = ImageState.ImageLoadSuccess(it)
        }
    }

}