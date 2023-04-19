@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
    Log.e("recompose", "ChatScreen!!!!!!!!")
    val state = gptViewModel.gptState.collectAsState()
    val text = remember {
        mutableListOf<String>()
    }
    gptViewModel.sendChat("where is korea?")

    ChatContent(state, text)
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    state: State<GptState>,
    text: MutableList<String>
) {
    Log.e("recompose", "ChatContent!!!!!!!!")
    val lazyRowScope = rememberLazyListState()
    val lazyRowItems = remember(state.value) { mutableStateListOf<String>() }

    when (state.value) {
        is GptState.Success -> {
            val data = state.value as GptState.Success
            LaunchedEffect(Unit) {
                data.chatData.collect{
                    lazyRowItems.add(it.choices[0].delta?.content ?: "")
                    lazyRowScope.scrollToItem(lazyRowItems.lastIndex)
                }
                Log.e("text", "$text")
            }
        }
        is GptState.End ->{
        }
        else -> {}
    }
    LazyRow(state = lazyRowScope) {
        items(lazyRowItems) {
            Text(text = it)
        }
    }
}