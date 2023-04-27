package com.example.feature_image_variation

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.gallery_search.GalleryManager

@Composable
fun ImageVariationScreen(navigator: NavHostController, context: Context) {
    SearchGalleryButton(context)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun SearchGalleryButton(context: Context) {
    val (selectedImagePath, selectedImageBitmap, getContent) = GalleryManager.Search(context)

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
    }
}

