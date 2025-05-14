package estga.dadm.athletrack.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.screens.LoginScreen
import estga.dadm.athletrack.screens.calendar.CalendarScreen
import estga.dadm.athletrack.screens.home.*
import estga.dadm.athletrack.components.*
import estga.dadm.athletrack.screens.*
import java.net.*

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
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            HomeScreenAtleta(
                user = user,
                navController = navController,
            )
        }

        composable("qrscan/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            QrScanScreen(user = user, navController = navController)
        }


        composable(
            "homeProfessor/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            HomeScreenProfessor(
                user = user,
                navController = navController,
            )
        }

        composable(
            "calendar/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = try {
                gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            if (user != null) {
                CalendarScreen(user = user)
            } else {
                // Trate o caso de erro, como redirecionar para outra tela ou exibir uma mensagem
            }
        }

    }
}
