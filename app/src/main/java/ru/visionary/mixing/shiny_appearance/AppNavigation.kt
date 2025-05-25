package ru.visionary.mixing.shiny_appearance


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
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
import ru.visionary.mixing.shiny_appearance.presentation.screen.WaitingScreen
import ru.visionary.mixing.shiny_appearance.presentation.viewmodel.AuthViewModel


@Composable
fun AppNavigation(authViewModel: AuthViewModel = hiltViewModel()) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val navController = rememberNavController()

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("mainTabsScreen?role=simpleUser&index=0") {
                popUpTo("loginScreen") { inclusive = true }
                popUpTo("splash_screen") { inclusive = true }
            }
        }
    }

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

        composable("waitingScreen?uuid={uuid}") { backStackEntry ->
            val uuid = backStackEntry.arguments?.getString("uuid") ?: ""
            WaitingScreen(navController, uuid)
        }

        composable("mainTabsScreen?index={index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            MainTabScreen(navController, index)
        }

        composable(
            route = "photoNeuralProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("uri")?.let { Uri.parse(it) }
            uri?.let { PhotoNeuralProcessingScreen(navController, it) }
        }

        composable(
            route = "photoPostProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("uri")?.let { Uri.parse(it) }
            uri?.let { PhotoPostProcessingScreen(navController, it) }
        }

        composable(
            route = "photoFinalProcessingScreen?uri={uri}",
            arguments = listOf(navArgument("uri") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val uri = backStackEntry.arguments?.getString("uri")?.let { Uri.parse(it) }
            uri?.let { PhotoFinalProcessingScreen(navController, it) }
        }
    }
}