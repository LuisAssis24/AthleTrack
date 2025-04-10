package estga.dadm.athletrack.screens

// Importações das bibliotecas Compose necessárias
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import estga.dadm.athletrack.ui.theme.*

// Função composable que representa o ecrã de login
@Composable
@Preview // Permite visualizar esta UI na aba "Design" do Android Studio
fun LoginScreen(
    onLoginClick: (String, String) -> Unit = { _, _ -> }, // Callback para o botão "Entrar"
    onForgotPasswordClick: () -> Unit = {} // Callback para o link "Esqueceste da palavra-passe?"
) {
    // Estados reativos que armazenam os valores dos campos de input
    var socio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Controla a visibilidade da password

    // Estrutura de layout principal em coluna, com alinhamento central
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa todo o ecrã
            .padding(32.dp), // Margens internas
        verticalArrangement = Arrangement.Center, // Centraliza verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
    ) {

        // Título da aplicação
        Text(
            text = "AthleTrack",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = White // Cor branca definida na paleta de cores
        )

        Spacer(modifier = Modifier.height(40.dp)) // Espaço entre título e campos

        // Campo de texto para o número de sócio
        OutlinedTextField(
            value = socio,
            onValueChange = { socio = it }, // Atualiza o estado ao escrever
            label = { Text("Número de Sócio") },
            modifier = Modifier.fillMaxWidth(), // Ocupa toda a largura disponível
            colors = OutlinedTextFieldDefaults.colors( // Personalização visual
                focusedBorderColor = BlueAccent,
                unfocusedBorderColor = GrayNeutral,
                focusedLabelColor = BlueAccent,
                unfocusedLabelColor = White,
                cursorColor = BlueAccent
            )
        )

        Spacer(modifier = Modifier.height(16.dp)) // Espaço entre os campos

        // Campo de texto para a password (sem ícone de visibilidade neste caso)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(Icons.Default.Visibility, contentDescription = "Mostrar ou ocultar password")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BlueAccent,
                unfocusedBorderColor = GrayNeutral,
                focusedLabelColor = BlueAccent,
                unfocusedLabelColor = White,
                cursorColor = BlueAccent
            )
        )

        Spacer(modifier = Modifier.height(24.dp)) // Espaço antes do botão

        // Botão de login
        Button(
            onClick = { onLoginClick(socio, password) }, // Chama a função de login com os dados introduzidos
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BlueAccent) // Cor de fundo do botão
        ) {
            Text("Entrar", color = White)
        }

        Spacer(modifier = Modifier.height(16.dp)) // Espaço antes do link

        // Link "Esqueceste da palavra-passe?"
        Text(
            text = "Esqueceste da palavra-passe?",
            color = White, // Cor da frase
            modifier = Modifier.clickable { onForgotPasswordClick() } // Torna a frase clicável
        )
    }
}
