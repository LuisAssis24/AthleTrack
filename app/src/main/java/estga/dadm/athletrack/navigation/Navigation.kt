package estga.dadm.athletrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import estga.dadm.athletrack.screens.home.HomeScreenAtleta
import estga.dadm.athletrack.screens.LoginScreen
import estga.dadm.athletrack.screens.home.HomeScreenProfessor

@Composable
fun AthleTrackNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = { socio, password ->
                    if (socio == "1" && password == "1") {
                        navController.navigate("homeAlteta")
                    } else {
                        navController.navigate("homeProfessor")
                    }
                },
                onForgotPasswordClick = {}
            )
        }
        composable("homeAtleta") {
            HomeScreenAtleta(userName = "João Atleta")
        }
        composable("homeProfessor") {
            HomeScreenProfessor(userName = "João Professor")
        }
    }
}
