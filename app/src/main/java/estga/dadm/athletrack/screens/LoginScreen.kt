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

@Composable
fun LoginScreen(
    onLoginClick: (User) -> Unit = {},
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val viewModel = remember { LoginViewModel(userPreferences) }

    var socio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var popupMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AthleTrack",
            style = typography.titleLarge,
            color = colorScheme.onPrimary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Acompanhe o seu treino",
            style = typography.displayLarge,
            color = colorScheme.primary
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = socio,
            onValueChange = { socio = it },
            label = { Text("Número de Sócio") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.onPrimary, // Alterado para onPrimary
                unfocusedBorderColor = colorScheme.secondary,
                focusedLabelColor =colorScheme.onPrimary, // Alterado para onPrimary
                unfocusedLabelColor = colorScheme.primary,
                cursorColor = colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(Icons.Default.Visibility, contentDescription = "Mostrar ou ocultar password")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =colorScheme.onPrimary, // Alterado para onPrimary
                unfocusedBorderColor =colorScheme.secondary,
                focusedLabelColor = colorScheme.onPrimary, // Alterado para onPrimary
                unfocusedLabelColor = colorScheme.primary,
                cursorColor = colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        val context = LocalContext.current

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
                                    // Aqui tratamos caso as credenciais estejam erradas
                                    popupMessage = msg.ifBlank { "Credenciais inválidas ou utilizador não encontrado." }
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