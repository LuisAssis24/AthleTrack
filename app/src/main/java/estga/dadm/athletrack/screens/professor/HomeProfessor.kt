package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.partials.QrCodeDialog
import estga.dadm.athletrack.ui.theme.*
import java.net.URLEncoder
import com.google.gson.Gson
import estga.dadm.athletrack.other.UserPreferences
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.SentimentSatisfied
import estga.dadm.athletrack.components.BottomMenu
import estga.dadm.athletrack.components.TopBar
import estga.dadm.athletrack.other.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeProfessor(
    user: User,
    navController: NavHostController,
    viewModel: HomeProfessorViewModel = viewModel()
) {
    var showQrCode by remember { mutableStateOf(false) }
    var qrCodeAtivo by remember { mutableStateOf("") }

    val aulasHoje by viewModel.treinosHoje.collectAsState()
    val aulasAmanha by viewModel.treinosAmanha.collectAsState()


    val selected = remember { mutableStateOf("hoje") }



    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    // Carregar as aulas assim que a composable for criada
    val isTimedOut = remember { mutableStateOf(false) }
    val isLoading = remember {
        derivedStateOf { aulasHoje.isEmpty() && !isTimedOut.value }
    }

    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
        kotlinx.coroutines.delay(500) // 5 segundos
        isTimedOut.value = true
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.surface),
        containerColor = colorScheme.surface
    ) { innerPadding ->
        LoadingScreen(isLoading = isLoading.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TopBar(
                    user = user,
                    navController = navController,
                    onBottomSheet = {
                        showBottomSheet = true
                    },
                    qrCodeClick = {

                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.7f)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = if (selected.value == "hoje") "Próximas Aulas Hoje" else "Aulas Amanhã",
                            style = Typography.titleMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            thickness = 1.5.dp,
                            color = colorScheme.secondary
                        )

                        val aulasParaMostrar =
                            if (selected.value == "hoje") aulasHoje else aulasAmanha

                        if (aulasParaMostrar.isEmpty()) {
                            Text(
                                text = "Sem aulas para ${if (selected.value == "hoje") "hoje" else "amanhã"}.",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center, // Centraliza verticalmente
                                modifier = Modifier
                                    .fillMaxHeight(0.6f) // Preenche 60% da altura
                                    .fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SentimentSatisfied,
                                    contentDescription = "Sem aulas",
                                    tint = colorScheme.primary,
                                    modifier = Modifier.size(148.dp) // Tamanho do smile
                                )
                            }
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
                                        style = Typography.bodyMedium,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center
                                    )
                                    if (index < aulasParaMostrar.size - 1) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            thickness = 1.dp,
                                            color = colorScheme.primary
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
                            shape = RoundedCornerShape(
                                topStart = 12.dp,
                                bottomStart = 12.dp
                            ), // Aumenta o arredondamento
                            icon = {}
                        ) {
                            Text(
                                "Hoje",
                                fontSize = 18.sp, // Aumenta o tamanho do texto
                                modifier = Modifier.padding(
                                    vertical = 5.dp,
                                    horizontal = 10.dp
                                ) // Aumenta o padding
                            )
                        }

                        SegmentedButton(
                            selected = selected.value == "amanha",
                            onClick = { selected.value = "amanha" },
                            shape = RoundedCornerShape(
                                topEnd = 12.dp,
                                bottomEnd = 12.dp
                            ), // Aumenta o arredondamento
                            icon = {}
                        ) {
                            Text(
                                "Amanhã",
                                fontSize = 18.sp, // Aumenta o tamanho do texto
                                modifier = Modifier.padding(
                                    vertical = 5.dp,
                                    horizontal = 10.dp
                                ) // Aumenta o padding
                            )
                        }
                    }
                }
            }
        }

        val scope = rememberCoroutineScope() // Necessário para o launch

        if (showBottomSheet) {
            BottomMenu(
                showBottomSheet = showBottomSheet,
                onDismiss = { showBottomSheet = false },
                bottomSheetState = bottomSheetState,
                scope = scope,
                onRefresh = {
                    viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
                },
                onLogout = {
                    userPreferences.clearLoginState()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }

        if (showQrCode) {
            QrCodeDialog(
                qrCode = qrCodeAtivo, onDismiss = { showQrCode = false },
                user = user, navController = navController
            )
        }
    }
}

