package estga.dadm.athletrack.screens

// Importações necessárias para UI e comportamento
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import estga.dadm.athletrack.ui.theme.* // Importa cores personalizadas
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(userName: String) {
    // Lista de treinos a exibir (simulação local)
    val treinos = listOf(
        "Treino Cardio - 02/04",
        "Treino Força - 04/04",
        "Treino Equipa - 06/04",
        "Ginásio Livre - 08/04",
        "Recuperação Ativa - 10/04",
        "Treino Extra - 11/04",
        "Treino Técnico - 12/04",
        "Treino Livre - 13/04",
        "Treino Tático - 14/04",
        "Avaliação Física - 15/04"
    ).take(10) // Limita a lista a 10 elementos

    // Estado do menu lateral (drawer)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope() // Necessário para abrir/fechar drawer com coroutine

    // Componente que permite ter um menu lateral (ModalNavigationDrawer)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Conteúdo do menu lateral
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Ocupa 70% da largura do ecrã
                    .fillMaxHeight()
                    .background(DarkGray)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // Ícone de perfil simulado
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(GrayNeutral),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto de Perfil",
                        tint = White,
                        modifier = Modifier.size(64.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nome do utilizador (passado como parâmetro)
                Text(
                    text = userName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        }
    ) {
        // Conteúdo principal do ecrã com Scaffold
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background), // Usa a cor de fundo do tema
            topBar = {
                // Barra superior com menu e calendário
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão de menu hambúrguer para abrir o drawer
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    // Ícone de calendário à direita
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendário",
                        tint = White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        ) { innerPadding ->
            // Corpo do ecrã
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
                    .wrapContentSize(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Título principal
                Text("Bem vindo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)

                Spacer(modifier = Modifier.height(40.dp))

                // Subtítulo
                Text("Próximos Treinos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)

                // Separador horizontal
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = GrayNeutral
                )

                // Listagem dos treinos centralizados
                treinos.forEach {
                    Text(
                        it,
                        fontSize = 16.sp,
                        color = White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = GrayNeutral)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botão de registar presença (ícone de câmara)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Câmara",
                            tint = White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Registar Presença", fontSize = 12.sp, color = White)
                }
            }
        }
    }
}

// Composable de pré-visualização para desenvolvimento no Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(userName = "João")
}