package estga.dadm.athletrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import estga.dadm.athletrack.screens.HomeScreen
import estga.dadm.athletrack.screens.LoginScreen

@Composable
fun AthleTrackNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = { socio, password ->
                    if (socio == "12345" && password == "admin") {
                        navController.navigate("home")
                    }
                },
                onForgotPasswordClick = {}
            )
        }
        composable("home") {
            HomeScreen(userName = "João Atleta")
        }
    }
}
