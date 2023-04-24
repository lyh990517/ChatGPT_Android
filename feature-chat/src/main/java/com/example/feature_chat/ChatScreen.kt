@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel

@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    val scrollState = rememberScrollState()
    val state = gptViewModel.gptState.collectAsState()
    Log.e("compose", "ChatScreen")
    LaunchedEffect(state.value) {
        when (state.value) {
            is GptState.LoadChat -> scrollState.animateScrollTo(scrollState.maxValue)
            is GptState.End -> {

            }
            is GptState.Error -> {

            }
            is GptState.Loading -> {

            }
            else -> {}
        }
    }
    ChatContent(scrollState, gptViewModel)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    scrollState: ScrollState,
    gptViewModel: ChatGPTViewModel
) {
    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()) {
        Column(
            Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(5f)
                .verticalScroll(scrollState)
        ) {
            Chat(gptViewModel)
        }
        Input(gptViewModel)
    }
}

@Composable
private fun InputStateLess(
    inputChange: (String) -> Unit,
    onSend: (String) -> Unit,
    onReset: (GptState) -> Unit,
    text: String
) {
    Log.e("compose", "InputStateLess")
    Row {
        TextField(
            value = text,
            onValueChange = inputChange,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = {
            onSend(text)
        }) {
            Text(text = "send")
        }
        Button(onClick = { onReset(GptState.End) }) {
            Text(text = "reset")
        }
    }
}

@Composable
private fun Input(
    gptViewModel: ChatGPTViewModel
) {
    with(gptViewModel) {
        InputStateLess(
            inputChange = inputChange,
            onSend = onSend,
            onReset = onReset,
            text = input.value
        )
    }
}

@Composable
private fun Chat(
    gptViewModel: ChatGPTViewModel,
) {
    Log.e("compose", "Chat")
    Text(text = gptViewModel.chatResult.collectAsState().value, fontSize = 18.sp)
}
