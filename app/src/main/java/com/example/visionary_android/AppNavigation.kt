package com.example.visionary_android

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.visionary_android.presentation.screen.AuthorizationScreen
import com.example.visionary_android.presentation.screen.LoginScreen
import com.example.visionary_android.presentation.screen.RegistrationScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "logInScreen") {

        composable("logInScreen") {
            LoginScreen(navController)
        }
        composable("registrationScreen") {
            RegistrationScreen(navController)
        }
        composable("authorizationScreen") {
            AuthorizationScreen(navController)
        }
    }
}