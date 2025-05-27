package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import estga.dadm.athletrack.ui.theme.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.other.FloatingPopupToast
import estga.dadm.athletrack.other.LoadingScreen
import kotlinx.coroutines.delay
import estga.dadm.athletrack.components.PasswordConfirmDialog
import java.time.LocalTime

@Composable
fun GestaoTreinosScreen(user: User, navController: NavHostController) {
    val viewModel: HomeProfessorViewModel = viewModel()
    val treinos by viewModel.treinosTodos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var treinoSelecionado by remember { mutableStateOf<Treino?>(null) }
    val context = LocalContext.current
    var horaSelecionada by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var modalidadeSelecionada by remember { mutableStateOf<Modalidade?>(null) }
    val modalidades by viewModel.modalidades.collectAsState()
    var dropdownExpanded by remember { mutableStateOf(false) }
    val diasSemana by viewModel.diasSemana.collectAsState()
    var diaSelecionado by remember { mutableStateOf<String?>(null) }
    var diaDropdownExpanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    var mensagem by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var popupMessage by remember { mutableStateOf("") }
    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var isToastSuccess by remember { mutableStateOf(true) }

    val isLoading = treinos.isEmpty()

    LaunchedEffect(Unit) {
        viewModel.carregarModalidades()
        viewModel.carregarDiasSemana()
        viewModel.carregarTodosOsTreinos(user.idSocio)
    }

    LoadingScreen(isLoading = isLoading) {
        Scaffold(
            topBar = {
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
                if (showToast && toastMessage != null) {
                    FloatingPopupToast(
                        message = toastMessage!!,
                        icon = if (isToastSuccess) Icons.Default.Check else Icons.Default.Warning,
                        color = if (isToastSuccess) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                    ) {
                        showToast = false
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Dia da Semana", color = colorScheme.primary)
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { diaDropdownExpanded = !diaDropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ),
                        border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(diaSelecionado ?: "Selecionar Dia")
                    }

                    DropdownMenu(
                        expanded = diaDropdownExpanded,
                        onDismissRequest = { diaDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f) // Define 90% da largura do pai
                            .background(colorScheme.primaryContainer) // aplica ao menu inteiro
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
                    border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                ) {
                    Text("Hora: ${horaSelecionada}")
                }

                Spacer(Modifier.height(8.dp))

                Text("Modalidade", color = colorScheme.primary)
                Box(Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { dropdownExpanded = !dropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ),
                        border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(modalidadeSelecionada?.nomeModalidade ?: "Selecionar Modalidade")
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        tonalElevation = 0.dp, // remove sombra escura
                        modifier = Modifier.fillMaxWidth(0.9f) // Define 90% da largura do pai
                            .background(colorScheme.primaryContainer) // aplica ao menu inteiro
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
                                    toastMessage = resposta
                                    isToastSuccess = sucesso
                                    showToast = true

                                    if (sucesso) {
                                        viewModel.carregarTodosOsTreinos(user.idSocio)
                                        // Limpar campos (opcional)
                                        diaSelecionado = null
                                        modalidadeSelecionada = null
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.background
                    )
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

                Text(
                    "Todos os Treinos",
                    color = colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Scroll apenas nesta parte
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    items(treinos) { treino ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "${treino.nomeModalidade} - ${treino.diaSemana} ${treino.hora}",
                                    color = colorScheme.primary
                                )
                                Text(
                                    "QR: ${treino.qrCode}",
                                    fontSize = 12.sp,
                                    color = colorScheme.primary,
                                )
                            }
                            IconButton(onClick = {
                                treinoSelecionado = treino
                                showDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Apagar",
                                    tint = colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

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
                    Toast.makeText(context, resposta, Toast.LENGTH_SHORT).show()
                    if (sucesso) {
                        viewModel.carregarTodosOsTreinos(user.idSocio)
                    }
                }
                showDialog = false
                treinoSelecionado = null
            }
        )
    }
}
