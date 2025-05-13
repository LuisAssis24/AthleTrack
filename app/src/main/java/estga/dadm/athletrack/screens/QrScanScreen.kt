package estga.dadm.athletrack.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.User

@Composable
fun QrScanScreen(user: User, navController: NavHostController) {
    var qrCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ler QR Code do Treino", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = qrCode,
            onValueChange = { qrCode = it },
            label = { Text("Código QR") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Aqui faremos o registo no backend posteriormente
            Toast.makeText(context, "Código lido: $qrCode", Toast.LENGTH_SHORT).show()
        }) {
            Text("Confirmar Presença")
        }
    }
}
