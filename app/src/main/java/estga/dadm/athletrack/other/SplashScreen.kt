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

/**
 * Componente que representa a tela de splash do aplicativo.
 *
 * A tela de splash é exibida por 1 segundo e, em seguida, redireciona o usuário
 * para a tela apropriada com base no estado de login e tipo de usuário.
 *
 * @param navController Controlador de navegação usado para gerenciar as transições entre telas.
 */
@Composable
fun SplashScreen(navController: NavHostController) {
    // Obtém o contexto atual e inicializa as preferências do usuário.
    val context = LocalContext.current
    val userPrefs = remember { UserPreferences(context) }
    val gson = remember { Gson() }

    // Coleta os estados das preferências do usuário.
    val isLoggedIn by userPrefs.isLoggedIn.collectAsState(initial = false)
    val userType by userPrefs.getUserType.collectAsState(initial = null)
    val userId by userPrefs.getUserId.collectAsState(initial = null)
    val userName by userPrefs.getUserName.collectAsState(initial = null)

    // Efeito lançado para gerenciar a navegação com base no estado do usuário.
    LaunchedEffect(isLoggedIn, userType, userId, userName) {
        delay(1000) // Mantém a tela de splash visível por 1 segundo.
        if (isLoggedIn && userId != null && userType != null && userName != null) {
            // Cria um objeto `User` e codifica seus dados em JSON.
            val user = User(idSocio = userId!!, tipo = userType!!, nome = userName!!)
            val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
            // Redireciona para a tela inicial apropriada com base no tipo de usuário.
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
            // Redireciona para a tela de login caso o usuário não esteja logado.
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Exibe um indicador de carregamento centralizado na tela.
    Box(
        modifier = Modifier
            .fillMaxSize() // Preenche toda a tela.
            .background(MaterialTheme.colorScheme.background), // Define a cor de fundo.
        contentAlignment = Alignment.Center // Centraliza o conteúdo.
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) // Indicador de progresso.
    }
}