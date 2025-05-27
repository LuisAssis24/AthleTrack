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
import androidx.compose.ui.unit.dp
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
import estga.dadm.athletrack.partials.QrCameraScanner
import com.google.gson.Gson
import estga.dadm.athletrack.components.BottomMenu
import estga.dadm.athletrack.components.TopBar
import estga.dadm.athletrack.other.LoadingScreen
import estga.dadm.athletrack.other.UserPreferences
import java.net.URLEncoder

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

    val isLoading =
        treinos.isEmpty() // Define se a tela está carregada com base na lista de treinos

    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
    }

    LoadingScreen(isLoading = isLoading) {
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

                TopBar(
                    user = user,
                    navController = navController,
                    onBottomSheet = { showBottomSheet = true },
                    qrCodeClick = {
                        // Verifica se a permissão da câmara está concedida
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                showCameraDialog = true
                            }
                            else -> {
                                // Solicita a permissão da câmara
                                launcher.launch(android.Manifest.permission.CAMERA)
                            }
                        }
                    }
                )

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
                            text = "Não tem treinos nos próximos dias.",
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
                                    .padding(vertical = 12.dp)
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
    }
}
