package estga.dadm.athletrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import estga.dadm.athletrack.api.LoginResponse
import estga.dadm.athletrack.screens.LoginScreen
import estga.dadm.athletrack.screens.home.HomeScreenAtleta
import estga.dadm.athletrack.screens.home.HomeScreenProfessor
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AthleTrackNavGraph(navController: NavHostController) {
    val gson = Gson()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = { user ->
                    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                    if (user.tipo == "atleta") {
                        navController.navigate("homeAtleta/$userJson")
                    } else {
                        navController.navigate("homeProfessor/$userJson")
                    }
                },
                onForgotPasswordClick = {}
            )
        }

        composable(
            "homeAtleta/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), LoginResponse::class.java)
            HomeScreenAtleta(user = user)
        }

        composable(
            "homeProfessor/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user =
                gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), LoginResponse::class.java)
            HomeScreenProfessor(user = user)
        }
    }
}
