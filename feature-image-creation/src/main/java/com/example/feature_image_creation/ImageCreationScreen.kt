package com.example.feature_image_creation

import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ImageCreationScreen(navigator: NavHostController) {
    Text(text = "Creation")
    BackHandler {
        navigator.popBackStack()
    }
}