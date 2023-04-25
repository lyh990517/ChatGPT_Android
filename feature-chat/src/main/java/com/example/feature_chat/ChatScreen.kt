@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    val lazyColumnState = LazyListState()
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
    ChatContent(
        gptViewModel, scrollState, isGenerating = isGenerating.value,
        onChange = {
            scope.launch {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        },
        onScroll = {
            scope.launch {
                lazyColumnState.animateScrollToItem(gptViewModel.chatList.size)
            }
        }, lazyListState = lazyColumnState
    )
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    gptViewModel: ChatGPTViewModel,
    scrollState: ScrollState,
    lazyListState: LazyListState,
    isGenerating: Boolean,
    onChange: () -> Unit,
    onScroll: () -> Unit
) {
    Log.e("compose", "ChatContent")
    Crossfade(targetState = isGenerating) { state ->
        when (state) {
            true -> OnCreateChat(scrollState, gptViewModel, onChange, isGenerating = isGenerating)
            false -> ChatList(onScroll, lazyListState, gptViewModel, isGenerating = isGenerating)
        }
    }
}

@Composable
private fun ChatList(
    onScroll: () -> Unit,
    lazyListState: LazyListState,
    gptViewModel: ChatGPTViewModel,
    isGenerating: Boolean
) {
    onScroll()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(gptViewModel.chatList) {
                Chat(text = it.chat, isUser = it.isUser, onScroll)
            }
        }
        Input(
            inputChange = gptViewModel.inputChange,
            onSend = gptViewModel.onSend,
            text = gptViewModel.input.value,
            isGenerating = isGenerating
        )
    }
}

@Composable
private fun OnCreateChat(
    scrollState: ScrollState,
    gptViewModel: ChatGPTViewModel,
    onChange: () -> Unit,
    isGenerating: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            Chat(
                gptViewModel.chatResult.collectAsState().value,
                onChange = onChange,
                isUser = false
            )
        }
        Input(
            inputChange = gptViewModel.inputChange,
            onSend = gptViewModel.onSend,
            text = gptViewModel.input.value,
            isGenerating = isGenerating
        )
    }
}

@Composable
private fun Input(
    inputChange: (String) -> Unit,
    onSend: (String) -> Unit,
    isGenerating: Boolean,
    text: String,
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
        }, enabled = !isGenerating) {
            Text(text = "send")
        }
    }
}

@Composable
private fun Chat(
    text: String,
    isUser: Boolean,
    onChange: () -> Unit
) {
    Log.e("compose", "Chat")
    val color =
        if (!isUser) Color(LocalContext.current.getColor(R.color.chat_back)) else Color.White
    val icon =
        if (!isUser) painterResource(id = R.drawable.gpt_icon) else painterResource(id = R.drawable.baseline_person_24)
    val profileBackGround = Color(LocalContext.current.getColor(R.color.chat_back))
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color)
    ) {
        val (profile, chat, line) = createRefs()
        Image(
            painter = icon,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .constrainAs(profile) {
                    start.linkTo(parent.start, margin = 20.dp)
                    top.linkTo(parent.top, margin = 20.dp)
                }
                .background(profileBackGround)
                .clip(RoundedCornerShape(10.dp))
        )
        Text(text = text, modifier = Modifier
            .constrainAs(chat) {
                start.linkTo(profile.end, margin = 10.dp)
                top.linkTo(profile.top, margin = 10.dp)
                end.linkTo(parent.end, margin = 20.dp)
                width = Dimension.fillToConstraints
            }
            .padding(bottom = 40.dp), fontSize = 14.sp)

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.LightGray)
            .constrainAs(line) {
                bottom.linkTo(parent.bottom)
            })
    }
    onChange()
}

@Preview
@Composable
fun ChatPreview() {
    Chat("hello?", isUser = true) {}
}
