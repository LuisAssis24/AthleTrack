package estga.dadm.athletrack.screens.home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun HomeScreenProfessor(userName: String) {
    // Dados de exemplo para as próximas aulas
    val aulasHoje = listOf(
        "Aula xxxx - 09:00",
        "Aula xxxxx- 10:30",
        "Aula xxxx - 14:00",
        "Aula xxxxx - 16:15"
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Seu conteúdo do menu aqui
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "9:41", // Hora atual
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendário",
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bem vindo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = userName,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Próximas Aulas Hoje",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    items(aulasHoje.size) { index ->
                        Column {
                            Text(
                                text = aulasHoje[index],
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                            if (index < aulasHoje.size - 1) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 1.dp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { /* Hoje */ }) {
                        Text("Hoje", fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = { /* Amanhã */ }) {
                        Text("Amanhã")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenProfessorPreview() {
    HomeScreenProfessor(userName = "Nome Sobrenome")
}