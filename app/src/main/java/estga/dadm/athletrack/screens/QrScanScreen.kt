package estga.dadm.athletrack.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Response
import okhttp3.ResponseBody
import javax.security.auth.callback.Callback

@Composable
fun QrScanScreen(user: User, navController: NavHostController) {
    var qrCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    val api = RetrofitClient.presencasService

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ler QR Code do Treino", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = qrCode,
                onValueChange = { qrCode = it },
                label = { Text("Código QR") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            val coroutineScope = rememberCoroutineScope()

            Button(onClick = {
                if (qrCode.isNotBlank()) {
                    coroutineScope.launch {
                        try {
                            val response = api.registarPresenca(PresencaRequest(user.idSocio, qrCode))
                            Toast.makeText(context, response.mensagem, Toast.LENGTH_LONG).show()

                            if (response.sucesso) {
                                navController.popBackStack()
                            }

                        } catch (e: Exception) {
                            Toast.makeText(context, "Erro ao registar presença: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    Toast.makeText(context, "Insere um código válido.", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Confirmar Presença")
            }

        }
    }
}