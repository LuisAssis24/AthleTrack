package estga.dadm.athletrack.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Componente que exibe uma tela de carregamento ou o conteúdo fornecido.
 *
 * @param isLoading Indica se o estado atual é de carregamento. Se `true`, exibe um indicador de progresso.
 * @param content Conteúdo a ser exibido quando o estado de carregamento for `false`.
 */
@Composable
fun LoadingScreen(isLoading: Boolean, content: @Composable () -> Unit) {
    if (isLoading) {
        // Exibe um indicador de progresso centralizado na tela.
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Exibe o conteúdo fornecido.
        content()
    }
}