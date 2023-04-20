@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel

@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    val state = gptViewModel.gptState.collectAsState()
    val scrollState = rememberScrollState()
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(5f)
                .verticalScroll(scrollState)
        ) {
            Chat(state, scrollState)
        }
        Input(gptViewModel, Modifier)
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun Input(viewModel: ChatGPTViewModel, modifier: Modifier) {
    val input = remember {
        mutableStateOf("")
    }
    Row(modifier) {
        TextField(
            value = input.value,
            onValueChange = { input.value = it },
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { viewModel.sendChat(input.value) }) {
            Text(text = "send")
        }
        Button(onClick = { viewModel.gptState.value = GptState.End }) {
            Text(text = "reset")
        }
    }
}

@Composable
private fun Chat(
    state: State<GptState>,
    scrollState: ScrollState
) {
    val chat = remember(state.value) { mutableStateOf("") }
    when (state.value) {
        is GptState.Success -> {
            val data = state.value as GptState.Success
            LaunchedEffect(Unit) {
                data.chatData.collect {
                    chat.value += it.choices[0].delta?.content ?: ""
                    scrollState.animateScrollTo(scrollState.maxValue)
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
    Text(text = chat.value, fontSize = 18.sp)
}