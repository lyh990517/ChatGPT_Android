package com.example.gallery_search

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

class GalleryManager {
    companion object{
        @Composable
        fun Search(context: Context): Triple<MutableState<String>, MutableState<ImageBitmap?>, ManagedActivityResultLauncher<String, Uri?>> {
            val selectedImagePath = remember { mutableStateOf("") }
            val selectedImageBitmap = remember { mutableStateOf<ImageBitmap?>(null) }
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    uri?.let {
                        selectedImagePath.value = uri.toString()
                        selectedImageBitmap.value =
                            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))
                                .asImageBitmap()
                    }
                }
            return Triple(selectedImagePath, selectedImageBitmap, launcher)
        }
    }
}