package estga.dadm.athletrack.screens.atleta

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import estga.dadm.athletrack.ui.theme.*
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeAtletaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.components.QrCameraScanner
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import estga.dadm.athletrack.other.UserPreferences
import java.net.URLEncoder
import estga.dadm.athletrack.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAtleta(
    user: User,
    navController: NavHostController,
    viewModel: HomeAtletaViewModel = viewModel()
) {
    val treinos by viewModel.treinos.collectAsState()
    val scope = rememberCoroutineScope()
    var showCameraDialog by remember { mutableStateOf(false) }
    val gson = Gson()

    val context = LocalContext.current

    val userPreferences = remember { UserPreferences(context) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCameraDialog = true
        } else {
            Toast.makeText(context, "Permissão da câmara é necessária", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
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

            // TOP BAR
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showBottomSheet = true }) {
                            Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )}

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = user.nome,
                                style = Typography.titleMedium,
                                color = colorScheme.primary
                            )
                            Text(
                                text = "Sócio nº ${user.idSocio}",
                                style = Typography.labelMedium,
                                color = colorScheme.secondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Botão QR Code
                        IconButton(onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    showCameraDialog = true
                                }

                                else -> {
                                    launcher.launch(android.Manifest.permission.CAMERA)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

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
                                tint = colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Próximos Treinos",
                style = Typography.titleMedium,
                color = colorScheme.primary,
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                if (treinos.isEmpty()) {
                    Text(
                        text = "Não tem treinos agendados para hoje.",
                        textAlign = TextAlign.Center,
                        color = colorScheme.primary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    )
                } else {
                    treinos.forEach {
                        Text(
                            "${it.nomeModalidade} - ${it.diaSemana} - ${it.hora}",
                            color = White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                        HorizontalDivider(color = colorScheme.secondary)
                    }
                }
            }

            // CÂMERA QR DIALOG
            // Flag de leitura única
            var leituraCompleta by remember { mutableStateOf(false) }

            // CÂMERA QR DIALOG
            if (showCameraDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showCameraDialog = false
                        leituraCompleta = false // permite nova leitura no futuro
                    },
                    confirmButton = {},
                    title = { Text("Ler QR Code") },
                    text = {
                        QrCameraScanner(onCodeScanned = { codigo ->
                            if (!leituraCompleta) {
                                leituraCompleta = true // impede leituras duplicadas

                                scope.launch {
                                    try {
                                        val response = viewModel.apiPresencas.registarPresenca(
                                            PresencaRequest(user.idSocio, codigo, true)
                                        )
                                        Toast.makeText(
                                            context,
                                            response.mensagem,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Erro: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } finally {
                                        showCameraDialog = false
                                        leituraCompleta = false
                                    }
                                }
                            }
                        })
                    }
                )
            }

        }
    }


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
}
