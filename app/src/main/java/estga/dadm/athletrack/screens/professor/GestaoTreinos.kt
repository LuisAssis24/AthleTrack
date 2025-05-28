package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import estga.dadm.athletrack.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.other.FloatingPopupToast
import estga.dadm.athletrack.other.LoadingScreen
import kotlinx.coroutines.delay
import estga.dadm.athletrack.partials.PasswordConfirmDialog
import estga.dadm.athletrack.components.TreinoItem
import estga.dadm.athletrack.other.SuccessPopupToast
import java.time.LocalTime

/**
 * Tela de gestão de treinos que permite criar novos treinos, listar todos os treinos e excluir treinos existentes.
 *
 * @param user Objeto do usuário logado, contendo informações como ID de sócio.
 * @param navController Controlador de navegação para gerenciar rotas entre telas.
 */
@Composable
fun GestaoTreinos(user: User, navController: NavHostController) {
    // ViewModel responsável por gerenciar os dados e ações da tela.
    val viewModel: HomeProfessorViewModel = viewModel()
    // Lista de treinos carregados.
    val treinos by viewModel.treinosTodos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var treinoSelecionado by remember { mutableStateOf<Treino?>(null) }
    val context = LocalContext.current
    var horaSelecionada by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var modalidadeSelecionada by remember { mutableStateOf<Modalidade?>(null) }
    val modalidades by viewModel.modalidades.collectAsState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    val diasSemana by viewModel.diasSemana.collectAsState()
    var diaSelecionado by remember { mutableStateOf<String?>(null) }
    var diaDropdownExpanded by remember { mutableStateOf(false) }

    var mensagem by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var isToastSuccess by remember { mutableStateOf(true) }
    var showSuccessPopup by remember { mutableStateOf(false) }

    // Define se a tela está carregando com base na lista de treinos.
    val isLoading = treinos.isEmpty()

    // Carrega os dados necessários ao iniciar a tela.
    LaunchedEffect(Unit) {
        viewModel.carregarModalidades()
        viewModel.carregarDiasSemana()
        viewModel.carregarTodosOsTreinos(user.idSocio)
    }

    // Exibe uma tela de carregamento enquanto os dados são carregados.
    LoadingScreen(isLoading = isLoading) {
        Scaffold(
            topBar = {
                // Barra superior com título e botão de voltar.
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Criar Treino",
                        style = Typography.displayLarge,
                        color = colorScheme.primary
                    )
                }
            },
            containerColor = colorScheme.surface
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                if (showPopup) {
                    FloatingPopupToast(
                        message = popupMessage,
                        icon = Icons.Default.Warning,
                        color = MaterialTheme.colorScheme.error
                    ) { showPopup = false }
                }

                if (showSuccessPopup) {
                    SuccessPopupToast(message = toastMessage) {
                        showSuccessPopup = false
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Campo para selecionar o dia da semana.
                Text("Dia da Semana", color = colorScheme.primary)
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { diaDropdownExpanded = !diaDropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ),
                        border = BorderStroke(1.dp, colorScheme.onPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(diaSelecionado ?: "Selecionar Dia")
                    }

                    DropdownMenu(
                        expanded = diaDropdownExpanded,
                        onDismissRequest = { diaDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(colorScheme.primaryContainer)
                    ) {
                        diasSemana.forEach { dia ->
                            DropdownMenuItem(
                                onClick = {
                                    diaSelecionado = dia
                                    diaDropdownExpanded = false
                                },
                                text = { Text(dia) }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Campo para selecionar a hora.
                Text("Hora", color = colorScheme.primary)
                OutlinedButton(
                    onClick = {
                        val timePicker = TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                horaSelecionada = LocalTime.of(hour, minute)
                            },
                            horaSelecionada.hour,
                            horaSelecionada.minute,
                            true
                        )
                        timePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.secondary
                    ),
                    border = BorderStroke(1.dp, colorScheme.onPrimary),
                ) {
                    Text("Hora: ${horaSelecionada}")
                }

                Spacer(Modifier.height(8.dp))

                // Campo para selecionar a modalidade.
                Text("Modalidade", color = colorScheme.primary)
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = !dropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ),
                        border = BorderStroke(1.dp, colorScheme.onPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(modalidadeSelecionada?.nomeModalidade ?: "Selecionar Modalidade")
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(colorScheme.primaryContainer)
                    ) {
                        modalidades.forEach { modalidade ->
                            DropdownMenuItem(
                                onClick = {
                                    modalidadeSelecionada = modalidade
                                    dropdownExpanded = false
                                },
                                text = { Text(modalidade.nomeModalidade) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Botão para criar um novo treino.
                Button(
                    onClick = {
                        when {
                            diaSelecionado.isNullOrBlank() -> {
                                popupMessage = "Seleciona um dia da semana"
                                showPopup = true
                            }

                            modalidadeSelecionada == null -> {
                                popupMessage = "Seleciona uma modalidade"
                                showPopup = true
                            }

                            horaSelecionada.toString().isBlank() -> {
                                popupMessage = "Seleciona uma hora"
                                showPopup = true
                            }

                            else -> {
                                viewModel.criarTreino(
                                    diaSemana = diaSelecionado!!,
                                    hora = horaSelecionada.toString(),
                                    idModalidade = modalidadeSelecionada!!.id,
                                    idProfessor = user.idSocio
                                ) { sucesso, resposta ->
                                    if (sucesso) {
                                        toastMessage = resposta
                                        showSuccessPopup = true

                                        // Limpar campos
                                        diaSelecionado = null
                                        modalidadeSelecionada = null

                                        viewModel.carregarTodosOsTreinos(user.idSocio)
                                    } else {
                                        popupMessage = resposta
                                        showPopup = true
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Criar Treino", color = colorScheme.background)
                }

                LaunchedEffect(mensagem) {
                    if (mensagem.isNotEmpty()) {
                        toastMessage = mensagem
                        showToast = true
                        delay(3000)
                        showToast = false
                        toastMessage = ""
                    }
                }

                Spacer(Modifier.height(24.dp))

                HorizontalDivider(color = colorScheme.secondary)

                // Título para a lista de treinos.
                Text(
                    "Todos os Treinos",
                    color = colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Lista de treinos existentes.
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    items(treinos) { treino ->
                        TreinoItem(
                            treino = treino,
                            onDeleteClick = {
                                treinoSelecionado = treino
                                showDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmação para excluir um treino.
    PasswordConfirmDialog(
        showDialog = showDialog && treinoSelecionado != null,
        descricao = "Tens a certeza que queres eliminar este treino: ${treinoSelecionado?.nomeModalidade} - ${treinoSelecionado?.diaSemana} ${treinoSelecionado?.hora}?",
        onDismiss = {
            showDialog = false
            treinoSelecionado = null
        },
        onConfirm = { passwordInput ->
            viewModel.apagarTreino(
                idSocio = user.idSocio,
                password = passwordInput,
                qrCode = treinoSelecionado!!.qrCode
            ) { sucesso, resposta ->
                if (sucesso) {
                    toastMessage = resposta
                    showSuccessPopup = true
                    viewModel.carregarTodosOsTreinos(user.idSocio)
                } else {
                    popupMessage = if (resposta.contains("senha", ignoreCase = true)) {
                        "Password inválida"
                    } else resposta
                    showPopup = true
                }
            }
            showDialog = false
            treinoSelecionado = null
        }
    )
}