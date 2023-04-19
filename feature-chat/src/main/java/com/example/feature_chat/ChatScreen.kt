package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.aallam.openai.api.BetaOpenAI
import com.example.presentation.state.GptState
import com.example.presentation.viewmodel.ChatGPTViewModel

@OptIn(BetaOpenAI::class)
@Composable
fun ChatScreen(navigator: NavHostController, gptViewModel: ChatGPTViewModel = hiltViewModel()) {
    Text("hi")
    val state = gptViewModel.gptState.collectAsState()
    gptViewModel.sendChat("where is korea?")
    when(state.value){
        is GptState.Success ->{
            val data = state.value as GptState.Success
            LaunchedEffect(Unit){
                data.chatData.collect{
                    Log.e("it","$it")
                }
            }
        }
        else ->{}
    }
    BackHandler {
        navigator.popBackStack()
    }
}