@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel
import kotlinx.coroutines.flow.collect

@OptIn(BetaOpenAI::class)
@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    Log.e("recompose","ChatScreen!!!!!!!!")
    val state = gptViewModel.gptState.collectAsState()
    val text = rememberSaveable {
        mutableStateOf("")
    }
    gptViewModel.sendChat("where is korea?")

    ChatContent(state,text)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    state: State<GptState>,
    text: MutableState<String>
) {
    Log.e("recompose","ChatContent!!!!!!!!")
    when (state.value) {
        is GptState.Success -> {
            val data = state.value as GptState.Success
            LaunchedEffect(Unit){
                data.chatData.collect{
                    text.value = it.choices[0].delta?.content ?: ""
                }
            }
        }
        else -> {}
    }
    Chat(text)
}

@Composable
private fun Chat(text: MutableState<String>) {
    Log.e("recompose","ChatContent!!!!!!!!")
    Text(text = text.value)
}