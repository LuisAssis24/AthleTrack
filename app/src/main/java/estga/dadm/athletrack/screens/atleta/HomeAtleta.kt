package estga.dadm.athletrack.screens.atleta

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.partials.QrCameraScanner
import com.google.gson.Gson
import estga.dadm.athletrack.partials.BottomMenu
import estga.dadm.athletrack.components.TopBar
import estga.dadm.athletrack.other.FloatingPopupToast
import estga.dadm.athletrack.other.LoadingScreen
import estga.dadm.athletrack.other.UserPreferences

/**
 * Tela principal do atleta que exibe os próximos treinos e permite interações como leitura de QR Code e logout.
 *
 * @param user Objeto do usuário contendo informações como ID de sócio.
 * @param navController Controlador de navegação para redirecionar o usuário entre telas.
 * @param viewModel ViewModel responsável por gerenciar os dados e ações da tela.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAtleta(
    user: User,
    navController: NavHostController,
    viewModel: HomeAtletaViewModel = viewModel()
) {
    // Estado que armazena os treinos carregados.
    val treinos by viewModel.treinos.collectAsState()
    val scope = rememberCoroutineScope()
    var showCameraDialog by remember { mutableStateOf(false) }
    val gson = Gson()

    val context = LocalContext.current

    // Estados para exibir mensagens de toast.
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var isToastSuccess by remember { mutableStateOf(true) }

    // Preferências do usuário para gerenciar estado de login.
    val userPreferences = remember { UserPreferences(context) }
    val bottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // Lançador para solicitar permissão de câmera.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCameraDialog = true
        } else {
            toastMessage = "Permissão da câmara é necessária"
            isToastSuccess = false
            showToast = true
        }
    }

    // Define se a tela está carregada com base na lista de treinos.
    val isLoading = treinos.isEmpty()

    // Carrega os treinos ao iniciar a tela.
    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
    }

    // Exibe a tela principal com treinos e interações.
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

                // Barra superior com opções de navegação e QR Code.
                TopBar(
                    user = user,
                    navController = navController,
                    onBottomSheet = { showBottomSheet = true },
                    qrCodeClick = {
                        // Verifica se a permissão da câmara está concedida.
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                android.Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                showCameraDialog = true
                            }

                            else -> {
                                // Solicita a permissão da câmara.
                                launcher.launch(android.Manifest.permission.CAMERA)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Título da seção de treinos.
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

                // Lista de treinos ou mensagem de ausência de treinos.
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

                // Diálogo de leitura de QR Code.
                var leituraCompleta by remember { mutableStateOf(false) }

                if (showCameraDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            showCameraDialog = false
                            leituraCompleta = false // Permite nova leitura no futuro.
                        },
                        confirmButton = {},
                        title = { Text("Ler QR Code") },
                        text = {
                            QrCameraScanner(onCodeScanned = { codigo ->
                                if (!leituraCompleta) {
                                    leituraCompleta = true // Impede leituras duplicadas.

                                    scope.launch {
                                        try {
                                            val response = viewModel.apiPresencas.registarPresenca(
                                                PresencaRequest(user.idSocio, codigo, true)
                                            )
                                            toastMessage = response.mensagem
                                            isToastSuccess = true
                                            showToast = true
                                        } catch (e: Exception) {
                                            toastMessage = "Aluno não inscrito nesta modalidade."
                                            isToastSuccess = false
                                            showToast = true
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

            // Exibe mensagens de toast.
            if (showToast) {
                FloatingPopupToast(
                    message = toastMessage,
                    icon = if (isToastSuccess) Icons.Default.Check else Icons.Default.Warning,
                    color = if (isToastSuccess) GreenSuccess else MaterialTheme.colorScheme.error
                ) {
                    showToast = false
                }
            }
        }

        // Exibe o menu inferior com opções de logout e atualização.
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