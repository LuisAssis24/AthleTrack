package estga.dadm.athletrack.other

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import estga.dadm.athletrack.ui.theme.Gray
import estga.dadm.athletrack.ui.theme.GreenSuccess
import kotlinx.coroutines.delay

/**
 * Componente que exibe um toast flutuante com ícone e mensagem.
 *
 * @param message Mensagem a ser exibida no toast.
 * @param icon Ícone a ser exibido ao lado da mensagem. Por padrão, utiliza o ícone de check.
 * @param color Cor de fundo do toast. Por padrão, utiliza a cor primária do tema.
 * @param onDismiss Função de callback executada ao fechar o toast.
 */
@Composable
fun FloatingPopupToast(
    message: String,
    icon: ImageVector = Icons.Default.Check,
    color: Color = colorScheme.primary,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 12.dp,
            color = color,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }
}

/**
 * Componente que exibe um toast de sucesso com ícone e mensagem.
 *
 * @param message Mensagem a ser exibida no toast.
 * @param onDismiss Função de callback executada ao fechar o toast.
 */
@Composable
fun SuccessPopupToast(
    message: String,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 12.dp,
            color = GreenSuccess, // Cor verde indicando sucesso.
            modifier = Modifier
                .padding(bottom = 32.dp)
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }
}

/**
 * Componente que exibe um toast simples com mensagem.
 *
 * @param message Mensagem a ser exibida no toast.
 * @param onDismiss Função de callback executada ao fechar o toast.
 */
@Composable
fun PopupToast(
    message: String,
    onDismiss: () -> Unit
) {
    Popup(
        alignment = Alignment.BottomCenter,
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 12.dp,
            color = Gray, // Cor cinza para o fundo do toast.
            modifier = Modifier
                .padding(bottom = 32.dp)
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(12.dp))
                Text(
                    text = message,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }
}