package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.ui.theme.*
import estga.dadm.athletrack.functions.QrCodeGenerator

@Composable
fun QrCodeDialog(qrCode: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight.copy(alpha = 0.5f)) // fundo semitransparente
            .clickable(onClick = onDismiss) // fechar ao clicar fora
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp)
                .background(White, shape = RoundedCornerShape(16.dp))
                .clickable(enabled = false) {}, // evita fechar ao clicar no conteúdo
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Código QR", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(16.dp))

                // qrCode
                QrCodeGenerator(data = qrCode)

            }
        }
    }
}




