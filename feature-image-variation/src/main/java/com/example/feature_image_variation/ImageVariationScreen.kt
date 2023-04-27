package com.example.feature_image_variation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.aallam.openai.api.BetaOpenAI
import com.example.gallery_search.GalleryManager
import com.example.image_downloader.ImageDownloader
import hello.yunho.presentation.state.ImageState
import hello.yunho.presentation.viewmodel.ImageViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(BetaOpenAI::class)
@Composable
fun ImageVariationScreen(navigator: NavHostController, context: Context) {
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
    SearchGalleryButton(context, viewModel, scope)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun SearchGalleryButton(context: Context, viewModel: ImageViewModel, scope: CoroutineScope) {
    val (selectedImagePath, selectedImageBitmap, getContent) = GalleryManager.Search(context)
    val path = remember { mutableStateOf(selectedImagePath.value) }
    Column {
        IconButton(
            onClick = {
                getContent.launch("image/*")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        }
        Text("Selected Image Path: ${selectedImagePath.value}")
        if (selectedImageBitmap.value != null) Image(
            bitmap = selectedImageBitmap.value!!,
            contentDescription = ""
        )
        IconButton(
            onClick = {
                viewModel.onMake(path.value)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Filled.Send, contentDescription = "Send")
        }
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
                id = R.drawable.ic_launcher_background
            )
        )
    }
}

