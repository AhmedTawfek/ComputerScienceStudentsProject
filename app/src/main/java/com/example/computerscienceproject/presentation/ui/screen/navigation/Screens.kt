package com.example.computerscienceproject.presentation.ui.screen.navigation

import com.example.computerscienceproject.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen{
    val route : String
    val name : String
}
interface ScreenAppBar{
    val selectedIcon : Int
    val unSelectedIcon : Int
}

@Serializable
data object Login : Screen {
    override val route: String
        get() = "login"
    override val name: String
        get() = "Login"
}

@Serializable
data object SignUp : Screen {
    override val route: String
        get() = "sign_up"
    override val name: String
        get() = "Sign Up"
}

@Serializable
data object UserInfo : Screen {
    override val route: String
        get() = "user_info"
    override val name: String
        get() = "User Info"
}

@Serializable
object Home : Screen, ScreenAppBar{
    override val route: String = "home"
    override val name: String = "Home"
    override val selectedIcon: Int = R.drawable.home_icon_selected
    override val unSelectedIcon: Int = R.drawable.home_not_selected_icon
}

@Serializable
object Chatbot : Screen, ScreenAppBar{
    override val route: String = "chat_bot"
    override val name: String = "Chat bot"
    override val selectedIcon: Int = R.drawable.chatbot_selected_icon
    override val unSelectedIcon: Int = R.drawable.chatbot_unselected_icon
}

@Serializable
object Calories : Screen, ScreenAppBar{
    override val route: String = "calories"
    override val name: String = "Calories"
    override val selectedIcon: Int = R.drawable.fire_selected_icon
    override val unSelectedIcon: Int = R.drawable.fire_unselected_icon
}

@Serializable
object Profile : Screen, ScreenAppBar{
    override val route: String = "profile"
    override val name: String = "Profile"
    override val selectedIcon: Int = R.drawable.user_filled_icon
    override val unSelectedIcon: Int = R.drawable.user_icon
}