package estga.dadm.athletrack.components

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import estga.dadm.athletrack.ui.theme.*

@Composable
fun QrCodeDialog(qrCode: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background.copy(alpha = 0.5f)) // fundo semitransparente
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

@Composable
fun QrCodeGenerator(data: String, size: Int = 512) {
    val bitmap = remember(data) {
        generateQrCodeBitmap(data, size)
    }

    bitmap?.let {
        Image(bitmap = it.asImageBitmap(), contentDescription = "QR Code")
    }
}

fun generateQrCodeBitmap(data: String, size: Int): Bitmap? {
    return try {
        val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size)
        val bmp = createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp[x, y] = if (bitMatrix.get(
                        x,
                        y
                    )
                ) Color.BLACK else Color.WHITE
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}




