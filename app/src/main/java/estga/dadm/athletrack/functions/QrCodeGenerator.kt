package estga.dadm.athletrack.functions

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

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
                ) android.graphics.Color.BLACK else android.graphics.Color.WHITE
            }
        }
        bmp
    } catch (e: Exception) {
        null
    }
}