package estga.dadm.athletrack.screens.professor

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Edit
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
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.components.QrCameraScanner
import androidx.compose.ui.res.painterResource

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

    val selected = remember { mutableStateOf("hoje") }

    val gson = Gson()
    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
    val scope = rememberCoroutineScope()
    var showCameraDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCameraDialog = true
        } else {
            Toast.makeText(context, "Permissão da câmara é necessária", Toast.LENGTH_SHORT).show()
        }
    }

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
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )

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

                        IconButton(onClick = {
                            Toast.makeText(context, "Calendário clicado", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendário",
                                tint = colorScheme.primary,
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
                        .background(colorScheme.surface, shape = RoundedCornerShape(16.dp))
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
                    .height(500.dp)
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

            // CÂMERA QR DIALOG
            /*if (showCameraDialog) {
                AlertDialog(
                    onDismissRequest = { showCameraDialog = false },
                    confirmButton = {},
                    title = { Text("Ler QR Code") },
                    text = {
                        QrCameraScanner(onCodeScanned = { codigo ->
                            scope.launch {
                                try {
                                    val response = viewModel.apiPresencas.registarPresenca(
                                        PresencaRequest(user.idSocio, codigo)
                                    )
                                    Toast.makeText(context, response.mensagem, Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    showCameraDialog = false
                                }
                            }
                        })
                    }
                )
            }*/
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
