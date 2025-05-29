package estga.dadm.athletrack.partials

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Componente que exibe um menu inferior com opções de atualização e logout.
 *
 * @param showBottomSheet Indica se o menu inferior deve ser exibido.
 * @param onDismiss Função chamada ao fechar o menu inferior.
 * @param bottomSheetState Estado do menu inferior utilizado para controlar sua visibilidade.
 * @param scope Escopo de corrotinas utilizado para executar ações assíncronas.
 * @param onRefresh Função suspensa chamada ao clicar na opção de atualizar.
 * @param onLogout Função suspensa chamada ao clicar na opção de logout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomMenu(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    onRefresh: suspend () -> Unit,
    onLogout: suspend () -> Unit,
) {
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = bottomSheetState,
            containerColor = colorScheme.primaryContainer,
            dragHandle = null
        ) {
            // Botão: Atualizar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            onRefresh()
                            onDismiss()
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Atualizar",
                    tint = colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Atualizar Treinos",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.primary
                )
            }

            // Botão: Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            onLogout()
                            onDismiss()
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.primary
                )
            }
        }
    }
}