package ru.visionary.mixing.shiny_appearance


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.visionary.mixing.shiny_appearance.presentation.screen.AuthorizationScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.LoginScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.MainTabScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.RegistrationScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "loginScreen") {

        composable("loginScreen") {
            LoginScreen(navController)
        }
        composable("registrationScreen") {
            RegistrationScreen(navController)
        }
        composable("authorizationScreen") {
            AuthorizationScreen(navController)
        }
        composable("mainTabsScreen?role={role}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "simpleUser"
            MainTabScreen(navController, role)
        }
    }
}