package com.example.feature_image_variation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun ImageVariationScreen(navigator: NavHostController) {
    Text(text = "ImageVariation")
    MyButton()
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun MyButton() {
    val context = LocalContext.current
    val selectedImagePath = remember { mutableStateOf("") }
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImagePath.value = uri.toString()
            }
        }

    Column {
        Button(
            onClick = {
                getContent.launch("image/*")
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("gallery")
        }
        Text("Selected Image Path: ${selectedImagePath.value}")
    }
}