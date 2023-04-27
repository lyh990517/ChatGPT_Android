package hello.yunho.chatgpt

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.feature_image_creation.ImageCreationScreen
import com.example.feature_image_edit.ImageEditScreen
import com.example.feature_image_variation.ImageVariationScreen
import hello.yunho.feature_chat.ChatScreen

@Composable
fun GPTApp(navigator: NavHostController = rememberNavController()) {
    NavHost(navController = navigator, startDestination = "Chat") {
        composable("Chat") {
            ChatScreen(navigator = navigator)
        }
        composable("Image Creation") {
            ImageCreationScreen(navigator = navigator)
        }
        composable("Image Edit") {
            ImageEditScreen(navigator = navigator)
        }
        composable("Image Variation") {
            ImageVariationScreen(navigator = navigator)
        }
    }
}