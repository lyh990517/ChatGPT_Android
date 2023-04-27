package hello.yunho.chatgpt

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hello.yunho.feature_chat.ChatScreen

@Composable
fun GPTApp(navigator: NavHostController = rememberNavController()) {
    NavHost(navController = navigator, startDestination = "Chat") {
        composable("Chat") {
            ChatScreen(navigator = navigator)
        }
        composable("Image Creation"){

        }
        composable("Image Edit"){

        }
        composable("Image Variation"){

        }
    }
}