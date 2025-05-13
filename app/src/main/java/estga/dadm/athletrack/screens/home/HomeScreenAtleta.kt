package estga.dadm.athletrack.screens.home

// Importações necessárias para UI e comportamento
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import java.net.URLEncoder
import estga.dadm.athletrack.ui.theme.*
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeAlunoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import com.google.gson.Gson
import estga.dadm.athletrack.components.MenuAtleta

@Composable

fun HomeScreenAtleta(
    user: User,
    navController: NavHostController,
    viewModel: HomeAlunoViewModel = viewModel()
) {
    val treinos by viewModel.treinos.collectAsState()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Carregar a viewmodel assim que a composable for criada
    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(
            user.idSocio,
            diaSemana = viewModel.detetarDiaSemana()
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            MenuAtleta(userName = user.nome)
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
                            tint = White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calendário",
                        tint = White,
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
                    .wrapContentSize(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                Text("Bem vindo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    "Próximos Treinos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.5.dp,
                    color = Gray
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(480.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (treinos.isEmpty()) {
                            Text(
                                text = "Não tem treinos agendados para hoje.",
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            treinos.take(12).forEachIndexed { index, treino ->
                                Text(
                                    text = "${treino.nomeModalidade} - ${treino.diaSemana} - ${treino.hora}",
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp),
                                    textAlign = TextAlign.Center
                                )
                                HorizontalDivider(thickness = 1.dp, color = Gray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Câmara",
                            tint = BluePrimary,
                            modifier = Modifier
                                .size(64.dp)
                                .clickable {
                                    val userJson = URLEncoder.encode(Gson().toJson(user), "UTF-8")
                                    navController.navigate("qrscan/$userJson")
                                }
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Registar Presença",
                        style = Typography.labelMedium,
                        )
                }
            }
        }
    }
}
