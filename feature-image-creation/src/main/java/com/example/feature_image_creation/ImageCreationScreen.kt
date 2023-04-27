package com.example.feature_image_creation

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.aallam.openai.api.BetaOpenAI
import com.example.image_downloader.ImageDownloader
import hello.yunho.presentation.state.ImageState
import hello.yunho.presentation.viewmodel.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(BetaOpenAI::class)
@Composable
fun ImageCreationScreen(
    context: Context,
    navigator: NavHostController,
) {
    val viewModel: ImageViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(state.value) {
        when (state.value) {
            is ImageState.Idle -> {}
            is ImageState.Error -> {}
            is ImageState.ImageLoadSuccess -> {
                val data = state.value as ImageState.ImageLoadSuccess
                viewModel.imageURL.value = data.images[0].url
            }
            else -> {}
        }
    }
    ImageContent(viewModel, scope, context)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun ImageContent(viewModel: ImageViewModel, scope: CoroutineScope, context: Context) {
    Column(Modifier.fillMaxSize()) {
        AsyncImage(
            model = viewModel.imageURL.value,
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .clickable {
                    scope.launch {
                        ImageDownloader.download(context, viewModel.imageURL.value)
                    }
                },
            placeholder = painterResource(
                id = R.drawable.gpt_icon
            )
        )
        Input(
            inputChange = viewModel.inputChange,
            onSend = viewModel.onSend,
            text = viewModel.input.value
        )
    }
}

@Composable
private fun Input(
    inputChange: (String) -> Unit,
    onSend: (String) -> Unit,
    text: String,
) {
    Log.e("compose", "InputStateLess")
    Box(Modifier.wrapContentHeight()) {
        OutlinedTextField(
            value = text,
            onValueChange = inputChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Message")
            }
        )
        IconButton(
            modifier = Modifier
                .wrapContentHeight()
                .size(50.dp)
                .align(Alignment.CenterEnd), onClick = {
                onSend(text)
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                Modifier.focusable(true)
            )
        }
    }
}