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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewParameter
import estga.dadm.athletrack.api.*
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class LoginResponsePreviewProvider : PreviewParameterProvider<LoginResponse> {
    override val values = sequenceOf(
        LoginResponse(
            idSocio = 1,
            nome = "João Silva",
            tipo = "professor",
        )
    )
}


@Preview
@Composable
fun PreviewHomeScreenProfessor(
    @PreviewParameter(LoginResponsePreviewProvider::class) user: LoginResponse
) {
    HomeScreenProfessor(user)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenProfessor(user: LoginResponse, viewModel: HomeProfessorViewModel = viewModel()) {

    val aulasHoje by viewModel.aulasHoje
    val aulasAmanha by viewModel.aulasAmanha

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val selected = remember { mutableStateOf("hoje") }

    // Carregar as aulas assim que a composable for criada
    LaunchedEffect(Unit) {
        viewModel.carregarAulas(user.nome)
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

                Text(
                    text = if (selected.value == "hoje") "Próximas Aulas Hoje" else "Próximas Aulas Amanhã",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                val aulasParaMostrar = if (selected.value == "hoje") aulasHoje else aulasAmanha

                if (aulasParaMostrar.isEmpty()) {
                    Text("Sem aulas para ${selected.value}.")
                } else {
                    aulasParaMostrar.forEachIndexed { index, aula ->
                        Column {
                            Text(
                                text = "${aula.nomeAula} - ${aula.hora}",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                            if (index < aulasParaMostrar.size - 1) {
                                Divider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    thickness = 1.dp
                                )
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
}
