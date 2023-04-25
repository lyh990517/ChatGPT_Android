@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    val state = gptViewModel.gptState.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isGenerating = remember { mutableStateOf(false) }
    Log.e("compose", "ChatScreen")
    LaunchedEffect(state.value) {
        when (state.value) {
            is GptState.LoadChat -> {
                isGenerating.value = true
            }
            is GptState.End -> {
                isGenerating.value = false
            }
            is GptState.Error -> {

            }
            is GptState.Loading -> {

            }
            else -> {}
        }
    }
    ChatContent(gptViewModel, scrollState, isGenerating.value) {
        scope.launch {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    gptViewModel: ChatGPTViewModel,
    scrollState: ScrollState,
    isGenerating: Boolean,
    onChange: () -> Unit
) {
    Log.e("compose", "ChatContent")
    Crossfade(targetState = isGenerating) { state ->
        when (state) {
            true -> {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Column(
                        Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .weight(5f)
                            .verticalScroll(scrollState)
                    ) {
                        Chat(gptViewModel, onChange = onChange)
                    }
                    Input(gptViewModel, Modifier)
                }
            }
            false -> {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    LazyColumn(Modifier.weight(1f)) {
                        items(gptViewModel.chatList) {
                            Text(text = it.chat)
                        }
                    }
                    Input(gptViewModel, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun InputStateLess(
    inputChange: (String) -> Unit,
    onSend: (String) -> Unit,
    text: String,
    modifier: Modifier
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
    }
}

@Composable
private fun Input(
    gptViewModel: ChatGPTViewModel,
    modifier: Modifier
) {
    with(gptViewModel) {
        InputStateLess(
            inputChange = inputChange,
            onSend = onSend,
            text = input.value,
            modifier = modifier
        )
    }
}

@Composable
private fun Chat(
    gptViewModel: ChatGPTViewModel,
    onChange: () -> Unit
) {
    Log.e("compose", "Chat")
    Text(text = gptViewModel.chatResult.collectAsState().value, fontSize = 18.sp)
    onChange()
}
