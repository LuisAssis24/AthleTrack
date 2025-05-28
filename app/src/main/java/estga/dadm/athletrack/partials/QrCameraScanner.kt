package estga.dadm.athletrack.partials

import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

/**
 * Componente que exibe uma interface de scanner de QR Code utilizando a câmera.
 *
 * @param onCodeScanned Função chamada quando um código QR é detectado, recebendo o valor do código como parâmetro.
 */
@Composable
fun QrCameraScanner(onCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    // Limita o tamanho da área da câmera (300x300dp).
    Box(
        modifier = Modifier
            .size(300.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )
    }

    // Configura a câmera e o analisador de QR Code.
    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val analyzer = ImageAnalysis.Builder().build().also {
            it.setAnalyzer(ContextCompat.getMainExecutor(context), QrAnalyzer { code ->
                onCodeScanned(code)
            })
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, analyzer)
    }
}

/**
 * Classe responsável por analisar imagens da câmera e detectar códigos QR.
 *
 * @param onQrCodeDetected Função chamada quando um código QR é detectado, recebendo o valor do código como parâmetro.
 */
class QrAnalyzer(val onQrCodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    /**
     * Método que analisa cada frame da câmera em busca de códigos QR.
     *
     * @param imageProxy Objeto que contém a imagem capturada pela câmera.
     */
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Processa a imagem para detectar códigos QR.
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let { code ->
                    onQrCodeDetected(code)
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}