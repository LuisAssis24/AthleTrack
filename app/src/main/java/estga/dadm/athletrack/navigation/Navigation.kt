package estga.dadm.athletrack.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.gson.Gson
import androidx.navigation.NavType
import androidx.navigation.navArgument
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.other.SplashScreen
import estga.dadm.athletrack.other.UserPreferences
import estga.dadm.athletrack.screens.*
import estga.dadm.athletrack.screens.calendar.*
import estga.dadm.athletrack.screens.atleta.*
import estga.dadm.athletrack.screens.professor.*
import java.net.*
import java.time.LocalDate

/**
 * Função que define o gráfico de navegação do aplicativo AthleTrack.
 *
 * @param navController Controlador de navegação usado para gerenciar as transições entre telas.
 */
@Composable
fun AthleTrackNavGraph(navController: NavHostController) {
    val gson = Gson()

    // Obtém o contexto atual e as preferências do usuário.
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val isLoggedIn = userPrefs.isLoggedIn.collectAsState(initial = false).value
    val userType = userPrefs.getUserType.collectAsState(initial = null).value
    val userId = userPrefs.getUserId.collectAsState(initial = null).value
    val userName = userPrefs.getUserName.collectAsState(initial = null).value.toString()

    // Efeito lançado para verificar o estado de login e navegar para a tela apropriada.
    LaunchedEffect(isLoggedIn, userType, userId) {
        if (isLoggedIn && userId != null && userType != null) {
            val user = User(
                idSocio = userId, tipo = userType,
                nome = userName,
            )
            val userJson = URLEncoder.encode(Gson().toJson(user), "UTF-8")
            if (user.tipo == "professor") {
                navController.navigate("homeProfessor/$userJson") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                navController.navigate("homeAtleta/$userJson") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    // Define as rotas de navegação.
    NavHost(navController, startDestination = "splash") {
        // Tela de splash.
        composable("splash") {
            SplashScreen(
                navController = navController,
            )
        }

        // Tela de login.
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

        // Tela inicial do atleta.
        composable(
            "homeAtleta/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            HomeAtleta(
                user = user,
                navController = navController,
            )
        }

        // Tela inicial do professor.
        composable(
            "homeProfessor/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = gson.fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            HomeProfessor(
                user = user,
                navController = navController,
            )
        }

        // Tela de gestão de treinos.
        composable(
            "gestaotreinos/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = Gson().fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            GestaoTreinos(user, navController)
        }

        // Tela de gestão de atletas.
        composable(
            route = "gestaoatletas/{userJson}",
            arguments = listOf(navArgument("userJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val user = Gson().fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            GestaoAtletas(user, navController)
        }

        // Tela de calendário.
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

        // Tela para adicionar evento ao calendário.
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
                AdicionarEventoScreen(
                    user = user,
                    navController = navController,
                    selectedDate = selectedDate
                )
            }
        }

        // Tela de gestão de presenças.
        composable(
            "gestaoPresencas/{userJson}/{qrCode}",
            arguments = listOf(
                navArgument("userJson") { type = NavType.StringType },
                navArgument("qrCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userJson = backStackEntry.arguments?.getString("userJson") ?: ""
            val qrCode = backStackEntry.arguments?.getString("qrCode") ?: ""
            val user = Gson().fromJson(URLDecoder.decode(userJson, "UTF-8"), User::class.java)
            GestaoPresencas(user = user, qrCode = qrCode, navController = navController)
        }
    }
}