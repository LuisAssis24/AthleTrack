package estga.dadm.athletrack.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordConfirmDialog(
    showDialog: Boolean,
    descricao: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                password = ""
                onDismiss()
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(password)
                        password = ""
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.error)
                ) {
                    Text("Eliminar", color = colorScheme.inversePrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    password = ""
                    onDismiss()
                }) {
                    Text("Cancelar", color = colorScheme.primary)
                }
            },
            title = {
                Text("Confirmar eliminação", color = colorScheme.primary)
            },
            text = {
                Column {
                    Text(
                        descricao,
                        color = colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text("Insere a tua password", color = colorScheme.secondary)
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            containerColor = colorScheme.surface
        )
    }
}
