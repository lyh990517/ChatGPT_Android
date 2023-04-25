package com.example.chatgpt

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature_chat.ChatScreen

@Composable
fun GPTApp(context: Context, navigator: NavHostController = rememberNavController()) {
    NavHost(navController = navigator, startDestination = "main") {
        composable("main") {
            ChatScreen(navigator = navigator, context = context)
        }
    }
}