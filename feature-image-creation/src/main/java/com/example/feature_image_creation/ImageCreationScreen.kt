package com.example.feature_image_creation

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.aallam.openai.api.BetaOpenAI
import hello.yunho.presentation.state.ImageState
import hello.yunho.presentation.viewmodel.ImageViewModel
import kotlinx.coroutines.launch

@OptIn(BetaOpenAI::class)
@Composable
fun ImageCreationScreen(
    context: Context,
    navigator: NavHostController,
) {
    val viewModel: ImageViewModel = hiltViewModel()
    Text(text = "Creation")
    val state = viewModel.uiState.collectAsState()
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
    ImageContent(viewModel,context)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun ImageContent(viewModel: ImageViewModel,context: Context) {
    val loader = ImageLoader(context)
    val req = ImageRequest.Builder(context)
        .data(viewModel.imageURL.value) // demo link
        .target { result ->
            val bitmap = (result as BitmapDrawable).bitmap
            saveImageToGallery(context, bitmap = bitmap, title = "test")
        }
        .build()
    LaunchedEffect(Unit){
        loader.execute(req)
    }
    Column(Modifier.fillMaxSize()) {
        AsyncImage(
            model = viewModel.imageURL.value,
            contentDescription = "",
            modifier = Modifier
                .weight(1f)
                .clickable {
//                    saveImageToGallery(context, bitmap = bitmap, title = "test")
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

private fun saveImageToGallery(context: Context, bitmap: Bitmap, title: String) {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val resolver: ContentResolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    try {
        uri?.let {
            resolver.openOutputStream(uri).use { out ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    throw Exception("Failed to save bitmap.")
                }
                Toast.makeText(context, "이미지가 다운로드 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: Exception) {
        Toast.makeText(context, "이미지 다운로드에 실패하였습니다.", Toast.LENGTH_SHORT).show()
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