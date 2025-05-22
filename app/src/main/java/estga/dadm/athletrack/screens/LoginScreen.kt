package estga.dadm.athletrack.screens

// Importações das bibliotecas Compose necessárias

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import estga.dadm.athletrack.api.UserRequest
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.other.UserPreferences
import estga.dadm.athletrack.viewmodels.LoginViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import estga.dadm.athletrack.ui.theme.*


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
            onClick = {viewModel.login(
                socio = socio.toInt(),
                password = password,
                onSuccess = { user -> onLoginClick(user) },
                onError = { msg -> Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
            )

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
}