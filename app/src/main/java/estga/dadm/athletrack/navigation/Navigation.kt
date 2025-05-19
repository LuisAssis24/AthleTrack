package estga.dadm.athletrack.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import androidx.navigation.NavType
import androidx.navigation.navArgument
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.screens.*
import estga.dadm.athletrack.screens.calendar.*
import estga.dadm.athletrack.screens.atleta.*
import estga.dadm.athletrack.screens.professor.*
import java.net.*
import java.time.LocalDate

@Composable
fun AthleTrackNavGraph(navController: NavHostController) {
    val gson = Gson()

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onLoginClick = { user ->
                    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                    if (user.tipo.lowercase() == "atleta") {
                        navController.navigate("homeAtleta/$userJson")
                    } else {
                        navController.navigate("homeProfessor/$userJson")
                    }
                },
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
            "gestaotreinos/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = Gson().fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            GestaoTreinosScreen(user, navController) // <-- passar o navController aqui
        }

        composable(
            route = "gestaoatletas/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = Gson().fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            GestaoAtletasScreen(user, navController)
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
                CalendarScreen(user = user, navController = navController)
            }
        }


        composable(
            "adicionarEvento/{userJson}/{selectedDate}",
            arguments = listOf(
                navArgument("userJson") { type = NavType.StringType },
                navArgument("selectedDate") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val selectedDateString = backStackEntry.arguments?.getString("selectedDate") ?: ""
            val user = try {
                gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            val selectedDate = try {
                LocalDate.parse(selectedDateString)
            } catch (e: Exception) {
                e.printStackTrace()
                LocalDate.now()
            }

            if (user != null) {
                AdicionarEventoScreen(user = user, navController = navController, selectedDate = selectedDate)
            }
        }

    }
}
