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
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import estga.dadm.athletrack.components.QrCodeDialog
import estga.dadm.athletrack.ui.theme.*
import java.net.URLEncoder
import com.google.gson.Gson
import estga.dadm.athletrack.other.UserPreferences
import kotlinx.coroutines.launch

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

    val gson = Gson()

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }


    // Carregar as aulas assim que a composable for criada
    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(
            user.idSocio,
            diaSemana = viewModel.detetarDiaSemana()
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.surface),
        containerColor = colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TOP BAR COM BORDA ARREDONDADA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LADO ESQUERDO: PERFIL + NOME
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showBottomSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(32.dp)
                            )
                        }


                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = user.nome,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = colorScheme.primary
                            )
                            Text(
                                text = "Sócio nº ${user.idSocio}",
                                fontSize = 14.sp,
                                color = colorScheme.secondary
                            )
                        }
                    }

                    // LADO DIREITO: ÍCONES DE AÇÃO
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        VerticalDivider(
                            modifier = Modifier
                                .height(24.dp)
                                .padding(horizontal = 8.dp),
                            color = colorScheme.secondary,
                            thickness = 1.dp
                        )

                        // Botão Calendário - redireciona para a tela de calendário
                        IconButton(onClick = {
                            val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                            navController.navigate("calendar/$userJson")
                        }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendário",
                                tint = White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val gson = Gson()
                        val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                        navController.navigate("gestaotreinos/$userJson")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
                        .size(81.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                        contentDescription = "GestaoTreinos",
                        modifier = Modifier.size(40.dp),
                        tint = colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestão de Treinos",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val gson = Gson()
                        val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                        navController.navigate("gestaoatletas/$userJson")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
                        .size(81.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "GestaoAtleta",
                        modifier = Modifier.size(40.dp),
                        tint = colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestão de Atletas",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.7f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = if (selected.value == "hoje") "Próximas Aulas Hoje" else "Aulas Amanhã",
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

    val scope = rememberCoroutineScope() // Necessário para o launch

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = colorScheme.primaryContainer, // COR DO DRAWER
            dragHandle = null // REMOVER A BARRA AZUL SUPERIOR
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            userPreferences.clearLoginState()
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        }
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Logout",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.primary
                )
            }
        }
    }

    if (showQrCode) {
        QrCodeDialog(qrCode = qrCodeAtivo, onDismiss = { showQrCode = false })
    }
}
