package estga.dadm.athletrack.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.components.MenuProfessor
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import estga.dadm.athletrack.components.QrCodeDialog
import estga.dadm.athletrack.ui.theme.*
import kotlinx.coroutines.launch
import java.net.URLEncoder
import com.google.gson.Gson

@Composable
fun HomeScreenProfessor(
    user: User,
    navController: NavHostController,
    viewModel: HomeProfessorViewModel = viewModel()
) {
    var showQrCode by remember { mutableStateOf(false) }
    var qrCodeAtivo by remember { mutableStateOf("") }

    val aulasHoje by viewModel.treinosHoje.collectAsState()
    val aulasAmanha by viewModel.treinosAmanha.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selected = remember { mutableStateOf("hoje") }

    val gson = Gson()
    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")

    // Carregar as aulas assim que a composable for criada
    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(
            user.idSocio,
            diaSemana = viewModel.detetarDiaSemana()
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuProfessor(user.nome)
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
                        text = "9:41", // (Opcional: hora real futuramente)
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendário",
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                navController.navigate("calendar/$userJson")
                            }
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
                horizontalAlignment = Alignment.CenterHorizontally
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if (selected.value == "hoje") "Próximas Aulas Hoje" else "Próximas Aulas Amanhã",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            thickness = 1.5.dp,
                            color = Gray
                        )

                        val aulasParaMostrar =
                            if (selected.value == "hoje") aulasHoje else aulasAmanha

                        if (aulasParaMostrar.isEmpty()) {
                            Text(
                                text = "Sem aulas para ${if (selected.value == "hoje") "hoje" else "amanhã"}.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            aulasParaMostrar.take(10).forEachIndexed { index, aula ->
                                Column(
                                    modifier = Modifier
                                        .clickable {
                                            qrCodeAtivo = aula.qrCode
                                            showQrCode = true
                                        }
                                ) {
                                    Text(
                                        text = "${aula.nomeModalidade} - ${aula.hora.take(5)}",
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    if (index < aulasParaMostrar.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            thickness = 1.dp,
                                            color = White
                                        )
                                    }
                                }

                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

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
    if (showQrCode) {
        QrCodeDialog(qrCode = qrCodeAtivo, onDismiss = { showQrCode = false })
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreenProfessor() {
    val user = User(
        idSocio = 1,
        nome = "Professor Exemplo",
        tipo = "Professor"
    )
    // Apenas para preview — passar um navController falso
    HomeScreenProfessor(
        user = user,
        navController = rememberNavController()
    )

}
