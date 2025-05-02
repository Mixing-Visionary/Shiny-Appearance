package ru.visionary.mixing.shiny_appearance


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.visionary.mixing.shiny_appearance.presentation.screen.AuthorizationScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.LoginScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.MainTabScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.PhotoFinalProcessingScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.PhotoNeuralProcessingScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.PhotoPostProcessingScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.RegistrationScreen
import ru.visionary.mixing.shiny_appearance.presentation.screen.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash_screen") {

        composable("splash_screen") {
            SplashScreen(navController)
        }
        composable("loginScreen") {
            LoginScreen(navController)
        }
        composable("registrationScreen") {
            RegistrationScreen(navController)
        }
        composable("authorizationScreen") {
            AuthorizationScreen(navController)
        }
        composable("mainTabsScreen?role={role}&index={index}") { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "simpleUser"
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            MainTabScreen(navController, index, role)
        }
        composable(
            route = "photoNeuralProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            val uri = uriString?.let { Uri.parse(it) }
            uri?.let { PhotoNeuralProcessingScreen(navController, it) }
        }
        composable(
            route = "photoPostProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            val uri = uriString?.let { Uri.parse(it) }
            uri?.let { PhotoPostProcessingScreen(navController, it) }
        }
        composable(
            route = "photoFinalProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("uri")
            val uri = uriString?.let { Uri.parse(it) }
            uri?.let { PhotoFinalProcessingScreen(navController, it) }
        }
    }
}