@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel

@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    val state = gptViewModel.gptState.collectAsState()
    Column (verticalArrangement = Arrangement.SpaceBetween){
        Chat(state)
        Input(gptViewModel)
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun Input(viewModel: ChatGPTViewModel) {
    val input = remember {
        mutableStateOf("")
    }
    Row {
        TextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { viewModel.sendChat(input.value) }) {
            Text(text = "send")
        }
    }
}

@Composable
private fun Chat(
    state: State<GptState>,
) {
    val chat = remember(state.value) { mutableStateOf("") }
    when (state.value) {
        is GptState.Success -> {
            val data = state.value as GptState.Success
            LaunchedEffect(Unit) {
                data.chatData.collect {
                    chat.value += it.choices[0].delta?.content ?: ""
                }
            }
        }
        is GptState.End -> {
        }
        is GptState.Error -> {
            val data = state.value as GptState.Error
            Log.e("error", "$data")
        }
        else -> {}
    }
    Text(text = chat.value)
}