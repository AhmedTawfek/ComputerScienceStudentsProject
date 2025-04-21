package com.example.computerscienceproject.presentation.ui.screen.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen{
    val route : String
    val name : String
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