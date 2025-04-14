package estga.dadm.athletrack.screens.home
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import estga.dadm.athletrack.api.LoginResponse
import estga.dadm.athletrack.components.MenuProfessor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenProfessor(user: LoginResponse) {



    val aulasHoje = listOf(
        "Aula xxxx - 09:00",
        "Aula xxxxx- 10:30",
        "Aula xxxx - 14:00",
        "Aula xxxxx - 16:15"
    )
    val aulasAmanha = listOf(
        "Aula xxxx - 10:00",
        "Aula xxxxx- 11:30",
        "Aula xxxx - 15:00",
        "Aula xxxxx - 17:15"
    )

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuProfessor(user.nome);
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
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally // centraliza conteúdos da coluna
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Bem vindo",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = user.nome,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Próximas Aulas Hoje",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de aulas
                aulasHoje.forEachIndexed { index, aula ->
                    Column {
                        Text(
                            text = aula,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            textAlign = TextAlign.Center
                        )
                        if (index < aulasHoje.size - 1) {
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                thickness = 1.dp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                val selected = remember { mutableStateOf("hoje") }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SingleChoiceSegmentedButtonRow {
                        SegmentedButton(
                            selected = selected.value == "hoje",
                            onClick = { selected.value = "hoje" },
                            shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                            icon = {}
                        ) {
                            Text("Hoje")
                        }

                        SegmentedButton(
                            selected = selected.value == "amanha",
                            onClick = { selected.value = "amanha" },
                            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                            icon = {}
                        ) {
                            Text("Amanhã")
                        }
                    }
                }


            }
        }
    }
}
