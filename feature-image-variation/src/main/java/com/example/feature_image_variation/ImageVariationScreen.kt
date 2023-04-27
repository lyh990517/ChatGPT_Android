package com.example.feature_image_variation

import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ImageVariationScreen(navigator: NavHostController) {
    Text(text = "ImageVariation")
    BackHandler {
        navigator.popBackStack()
    }
}