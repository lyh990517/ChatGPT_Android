package hello.yunho.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.BetaOpenAI
import dagger.hilt.android.lifecycle.HiltViewModel
import hello.yunho.domain.usecase.CreateImageUseCase
import hello.yunho.domain.usecase.EditImageUseCase
import hello.yunho.domain.usecase.MakeVariationImageUseCase
import hello.yunho.presentation.state.ImageState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okio.source
import javax.inject.Inject

@OptIn(BetaOpenAI::class)
@HiltViewModel
class ImageViewModel @Inject constructor(
    private val createImageUseCase: CreateImageUseCase,
    private val makeVariationImageUseCase: MakeVariationImageUseCase,
    private val editImageUseCase: EditImageUseCase
) :
    ViewModel() {
    private val _uiState = MutableStateFlow<ImageState>(ImageState.Idle)
    val uiState = _uiState

    val imageURL = mutableStateOf("")
    val input = mutableStateOf("")
    val fileSource = mutableStateOf("")
    val onSend: (String) -> Unit = {
        createImage(input.value, 1)
        input.value = ""
    }
    val onMake: (String) -> Unit = {
        makeImageVariation(fileSource.value, 1)
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

    private fun makeImageVariation(fileSource: String, numberOfImage: Int) = viewModelScope.launch {
        Log.e("file","${fileSource}")
        makeVariationImageUseCase.invoke(fileSource, numberOfImage).catch {
            Log.e("it", "${it.message}")
            _uiState.value = ImageState.Error(it)
        }.collect {
            _uiState.value = ImageState.ImageLoadSuccess(it)
        }
    }
}