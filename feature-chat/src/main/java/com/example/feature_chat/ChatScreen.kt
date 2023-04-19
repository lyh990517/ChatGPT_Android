@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel

@OptIn(BetaOpenAI::class)
@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    Log.e("recompose", "ChatScreen!!!!!!!!")
    val state = gptViewModel.gptState.collectAsState()
    gptViewModel.sendChat("where is korea?")

    ChatContent(state)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    state: State<GptState>,
) {
    val lazyRowItems = remember(state.value) { mutableStateOf("") }
    Column {
        when (state.value) {
            is GptState.Success -> {
                val data = state.value as GptState.Success
                LaunchedEffect(Unit) {
                    data.chatData.collect {
                        lazyRowItems.value += it.choices[0].delta?.content ?: ""
                    }
                }
            }
            is GptState.End -> {
            }
            else -> {}
        }
        Text(text = lazyRowItems.value)
    }
}