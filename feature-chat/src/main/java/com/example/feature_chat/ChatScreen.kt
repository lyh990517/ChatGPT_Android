@file:OptIn(BetaOpenAI::class)

package com.example.feature_chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun ChatScreen(
    context: Context,
    navigator: NavHostController,
    gptViewModel: ChatGPTViewModel = hiltViewModel()
) {
    val state = gptViewModel.gptState.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isGenerating = remember { mutableStateOf(false) }
    Log.e("compose", "ChatScreen")
    LaunchedEffect(state.value) {
        when (state.value) {
            is GptState.Idle -> {

            }
            is GptState.Loading -> {
                isGenerating.value = true
            }
            is GptState.LoadChat -> {
                isGenerating.value = true
            }
            is GptState.End -> {
                isGenerating.value = false
                gptViewModel.gptState.value = GptState.Idle
            }
            is GptState.Error -> {

            }
            else -> {}
        }
    }
    ChatContent(
        context,
        gptViewModel, scrollState, isGenerating = isGenerating.value,
        onChange = {
            scope.launch {
                scrollState.animateScrollTo(scrollState.maxValue)
            }
        }
    )
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
private fun ChatContent(
    context: Context,
    gptViewModel: ChatGPTViewModel,
    scrollState: ScrollState,
    isGenerating: Boolean,
    onChange: () -> Unit,
) {
    Log.e("compose", "ChatContent")
    Crossfade(targetState = isGenerating) { state ->
        when (state) {
            true -> OnCreateChat(context,scrollState, gptViewModel, onChange, isGenerating = isGenerating)
            false -> ChatList(gptViewModel = gptViewModel, isGenerating = isGenerating, context = context)
        }
    }
}

@Composable
private fun ChatList(
    context: Context,
    gptViewModel: ChatGPTViewModel,
    isGenerating: Boolean
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        LazyColumn(
            state = LazyListState(firstVisibleItemIndex = gptViewModel.chatList.size),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(gptViewModel.chatList) {
                Chat(text = it.chat, isUser = it.isUser, context = context)
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
    context: Context,
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
                context,
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
    Box(Modifier.wrapContentHeight()) {
        OutlinedTextField(
            value = text,
            onValueChange = inputChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Message")
            },
            enabled = !isGenerating
        )
        IconButton(
            modifier = Modifier
                .wrapContentHeight()
                .size(50.dp)
                .align(Alignment.CenterEnd), onClick = {
                onSend(text)
            }, enabled = !isGenerating
        ) {
            Icon(
                imageVector = Icons.Filled.Send,
                contentDescription = "Send",
                Modifier.focusable(true)
            )
        }
    }
}

@Composable
private fun Chat(
    context: Context,
    text: String,
    isUser: Boolean,
    onChange: () -> Unit = {}
) {
    Log.e("compose", "Chat")
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", text)
    val color = if (!isUser) Color(LocalContext.current.getColor(R.color.chat_back)) else Color.White
    val icon = if (!isUser) painterResource(id = R.drawable.gpt_icon) else painterResource(id = R.drawable.baseline_person_24)
    val profileBackGround = Color(LocalContext.current.getColor(R.color.chat_back))
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color)
    ) {
        val (profile, chat, line, copy) = createRefs()
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
        SelectionContainer(modifier = Modifier
            .constrainAs(chat) {
                start.linkTo(profile.end, margin = 10.dp)
                top.linkTo(profile.top, margin = 10.dp)
                end.linkTo(parent.end, margin = 20.dp)
                width = Dimension.fillToConstraints
            }) {
            Text(text = text, fontSize = 16.sp)
        }
        Image(
            painter = painterResource(id = R.drawable.baseline_content_copy_24),
            contentDescription = "copy",
            modifier = Modifier
                .clickable {
                    clipboardManager.setPrimaryClip(clipData)
                    Toast.makeText(context, "복사되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .constrainAs(copy) {
                    end.linkTo(chat.end)
                    top.linkTo(chat.bottom, margin = 30.dp)
                }
                .padding(bottom = 40.dp)
        )
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
    Chat(context = LocalContext.current,"hello?", isUser = true) {}
}

@Preview
@Composable
fun InputPreview() {
    Input(
        inputChange = {},
        onSend = {},
        text = "",
        isGenerating = false
    )
}
