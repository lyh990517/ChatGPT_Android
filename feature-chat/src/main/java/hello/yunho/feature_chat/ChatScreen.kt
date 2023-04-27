package hello.yunho.feature_chat

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
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
import hello.yunho.presentation.state.ChatState
import hello.yunho.presentation.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navigator: NavHostController,
) {
    val gptViewModel: ChatViewModel = hiltViewModel()
    val state = gptViewModel.gptState.collectAsState()
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val isGenerating = remember { mutableStateOf(false) }
    Log.e("compose", "ChatScreen")
    LaunchedEffect(state.value) {
        when (state.value) {
            is ChatState.Idle -> {

            }
            is ChatState.Loading -> {
                isGenerating.value = true
            }
            is ChatState.LoadChat -> {
                isGenerating.value = true
            }
            is ChatState.End -> {
                isGenerating.value = false
                gptViewModel.gptState.value = ChatState.Idle
            }
            is ChatState.Error -> {

            }
            else -> {}
        }
    }
    Scaffold(
        drawerContent = {
            Drawer(navigator)
        }, drawerBackgroundColor = Color.Black,
        drawerElevation = 10.dp
    ) {
        ChatContent(
            gptViewModel, scrollState, isGenerating = isGenerating.value,
            onChange = {
                scope.launch {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        )
    }
    BackHandler {
        navigator.popBackStack()
    }
}

@Composable
fun Drawer(navigator: NavHostController) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        DrawerItem(text = "Image Creation", navigator)
        DrawerItem(text = "Image Edit", navigator)
        DrawerItem(text = "Image Variation", navigator)
    }
}

@Composable
fun DrawerItem(text: String, navigator: NavHostController) {
    Card(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .fillMaxWidth()
            .height(80.dp),
        elevation = 5.dp,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(width = 1.dp, color = Color.White),
        backgroundColor = Color.Black
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable { navigator.navigate(text) }) {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun ChatContent(
    gptViewModel: ChatViewModel,
    scrollState: ScrollState,
    isGenerating: Boolean,
    onChange: () -> Unit,
) {
    Log.e("compose", "ChatContent")
    Crossfade(targetState = isGenerating) { state ->
        when (state) {
            true -> OnCreateChat(scrollState, gptViewModel, onChange, isGenerating = isGenerating)
            false -> ChatList(gptViewModel = gptViewModel, isGenerating = isGenerating)
        }
    }
}

@Composable
private fun ChatList(
    gptViewModel: ChatViewModel,
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
                Chat(text = it.chat, isUser = it.isUser)
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
    gptViewModel: ChatViewModel,
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
    text: String,
    isUser: Boolean,
    onChange: () -> Unit = {}
) {
    Log.e("compose", "Chat")
    val clipboardManager =
        LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", text)
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
    Chat("hello?", isUser = true) {}
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
