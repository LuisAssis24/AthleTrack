package estga.dadm.athletrack.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.gson.Gson
import estga.dadm.athletrack.api.User
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import java.net.URLEncoder


@Composable
fun SplashScreen(navController: NavHostController) {
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val gson = remember { Gson() }

    val isLoggedIn by userPrefs.isLoggedIn.collectAsState(initial = false)
    val userType by userPrefs.getUserType.collectAsState(initial = null)
    val userId by userPrefs.getUserId.collectAsState(initial = null)
    val userName by userPrefs.getUserName.collectAsState(initial = null)

    LaunchedEffect(isLoggedIn, userType, userId, userName) {
        delay(1000) // Splash visível por 1s
        if (isLoggedIn && userId != null && userType != null && userName != null) {
            val user = User(idSocio = userId!!, tipo = userType!!, nome = userName!!)
            val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
            if (user.tipo == "professor") {
                navController.navigate("homeProfessor/$userJson") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("homeAtleta/$userJson") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Simples loading visual
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}


