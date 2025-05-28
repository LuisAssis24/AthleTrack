package estga.dadm.athletrack.partials

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Componente que exibe um diálogo para confirmação de eliminação com inserção de senha.
 *
 * @param showDialog Indica se o diálogo deve ser exibido.
 * @param descricao Texto descritivo exibido no diálogo.
 * @param onDismiss Função chamada ao fechar o diálogo.
 * @param onConfirm Função chamada ao confirmar a ação, recebendo a senha inserida como parâmetro.
 */
@Composable
fun PasswordConfirmDialog(
    showDialog: Boolean,
    descricao: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    // Estado para armazenar a senha inserida pelo usuário.
    var password by remember { mutableStateOf("") }

    // Exibe o diálogo apenas se showDialog for verdadeiro.
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                password = "" // Limpa a senha ao fechar o diálogo.
                onDismiss()
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(password) // Chama a função de confirmação com a senha inserida.
                        password = "" // Limpa a senha após a confirmação.
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
                ) {
                    Text("Eliminar", color = colorScheme.primary) // Botão de confirmação.
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    password = "" // Limpa a senha ao cancelar.
                    onDismiss()
                }) {
                    Text("Cancelar", color = colorScheme.primary) // Botão de cancelamento.
                }
            },
            title = {
                Text("Confirmar eliminação", color = colorScheme.primary) // Título do diálogo.
            },
            text = {
                Column {
                    Text(
                        descricao, // Exibe a descrição fornecida.
                        color = colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = password, // Campo de texto para inserir a senha.
                        onValueChange = { password = it },
                        label = {
                            Text("Insere a tua password", color = colorScheme.secondary)
                        },
                        singleLine = true, // Limita o campo a uma única linha.
                        visualTransformation = PasswordVisualTransformation(), // Oculta o texto inserido.
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = colorScheme.surface // Define a cor de fundo do diálogo.
        )
    }
}