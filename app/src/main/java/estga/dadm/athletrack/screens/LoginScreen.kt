package estga.dadm.athletrack.screens

// Importações das bibliotecas Compose necessárias

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.other.UserPreferences
import estga.dadm.athletrack.viewmodels.LoginViewModel
import estga.dadm.athletrack.other.FloatingPopupToast

/**
 * Tela de login que permite ao usuário inserir suas credenciais para autenticação.
 *
 * @param onLoginClick Função de callback chamada após o login bem-sucedido, recebendo o objeto `User`.
 */
@Composable
fun LoginScreen(
    onLoginClick: (User) -> Unit = {},
) {
    // Contexto da aplicação para exibir mensagens e acessar preferências.
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val viewModel = remember { LoginViewModel(userPreferences) }

    // Estados para armazenar os valores dos campos de entrada.
    var socio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Estados para exibir mensagens de erro ou popups.
    var popupMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    // Layout principal da tela.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título do aplicativo.
        Text(
            text = "AthleTrack",
            style = typography.displayLarge,
            color = colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Acompanhe o seu treino",
            style = typography.titleLarge,
            color = colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de entrada para o número de sócio.
        OutlinedTextField(
            value = socio,
            onValueChange = { socio = it },
            label = { Text("Número de Sócio") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.onPrimary,
                unfocusedBorderColor = colorScheme.secondary,
                focusedLabelColor = colorScheme.onPrimary,
                unfocusedLabelColor = colorScheme.primary,
                cursorColor = colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de entrada para a senha.
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        Icons.Default.Visibility,
                        contentDescription = "Mostrar ou ocultar password"
                    )
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.onPrimary,
                unfocusedBorderColor = colorScheme.secondary,
                focusedLabelColor = colorScheme.onPrimary,
                unfocusedLabelColor = colorScheme.primary,
                cursorColor = colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão para realizar o login.
        Button(
            onClick = {
                when {
                    socio.isBlank() -> {
                        popupMessage = "Insere o ID de sócio"
                        showPopup = true
                    }

                    password.isBlank() -> {
                        popupMessage = "Insere a palavra-passe"
                        showPopup = true
                    }

                    else -> {
                        try {
                            viewModel.login(
                                socio = socio.toInt(),
                                password = password,
                                onSuccess = { user -> onLoginClick(user) },
                                onError = { msg ->
                                    // Exibe mensagem de erro caso as credenciais estejam erradas.
                                    popupMessage =
                                        msg.ifBlank { "Credenciais inválidas ou utilizador não encontrado." }
                                    showPopup = true
                                }
                            )
                        } catch (e: Exception) {
                            popupMessage = "Erro de autenticação: ${e.message}"
                            showPopup = true
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text(
                text = "Entrar",
                style = typography.labelMedium,
                color = colorScheme.background
            )
        }
    }

    // Exibe um popup de erro, caso necessário.
    if (showPopup) {
        FloatingPopupToast(
            message = popupMessage,
            icon = Icons.Default.Warning,
            color = MaterialTheme.colorScheme.error
        ) {
            showPopup = false
        }
    }
}