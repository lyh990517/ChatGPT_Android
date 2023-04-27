package com.example.feature_image_edit

import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ImageEditScreen(navigator: NavHostController){
    Text(text = "ImageEdit")
    BackHandler {
        navigator.popBackStack()
    }
}