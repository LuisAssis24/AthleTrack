package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserCreate
import estga.dadm.athletrack.viewmodels.GestaoAtletasViewModel
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.ui.Modifier
import estga.dadm.athletrack.components.AtletaItem
import estga.dadm.athletrack.other.FloatingPopupToast
import estga.dadm.athletrack.partials.PasswordConfirmDialog
import estga.dadm.athletrack.other.LoadingScreen
import estga.dadm.athletrack.other.SuccessPopupToast
import estga.dadm.athletrack.ui.theme.*

@Composable
fun GestaoAtletasScreen(user: User, navController: NavHostController) {
    val viewModel: GestaoAtletasViewModel = viewModel()
    val atletas by viewModel.atletas.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val modalidadesSelecionadas = remember { mutableStateListOf<Int>() }
    var showModalidadesMenu by remember { mutableStateOf(false) }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var atletaParaApagar by remember { mutableStateOf<User?>(null) }

    val listaModalidades by viewModel.modalidades.collectAsState()

    val isLoading = atletas.isEmpty()
    var toastMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    var isToastSuccess by remember { mutableStateOf(true) }
    var popupMessage by remember { mutableStateOf("") }
    var showPopup by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }
    var showSuccessPopup by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        try {
            viewModel.carregarAtletas()
            viewModel.carregarModalidades()
        } catch (e: Exception) {
            println("Erro ao carregar dados: ${e.message}")
        }
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = colorScheme.primary
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Gestão de Atletas",
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
                    .padding(padding)
            ) {
                if (showToast && toastMessage.isNotEmpty()) {
                    FloatingPopupToast(
                        message = toastMessage,
                        icon = if (isToastSuccess) Icons.Default.Check else Icons.Default.Warning,
                        color = if (isToastSuccess) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
                    ) {
                        showToast = false
                    }
                }
            }

            if (showPopup) {
                FloatingPopupToast(
                    message = popupMessage,
                    icon = Icons.Default.Warning,
                    color = MaterialTheme.colorScheme.error
                ) {
                    showPopup = false
                }
            }
            if (showSuccessPopup) {
                SuccessPopupToast(
                    message = successMessage
                ) {
                    showSuccessPopup = false
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Atleta") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.secondary,
                        cursorColor = colorScheme.primary
                    )
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.secondary,
                        cursorColor = colorScheme.primary
                    )
                )
                Spacer(Modifier.height(8.dp))

                Text(
                    "Modalidades",
                    color = colorScheme.primary
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { showModalidadesMenu = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = colorScheme.primaryContainer)
                    ) {
                        Text(
                            text = if (modalidadesSelecionadas.isEmpty()) "Selecionar Modalidades" else modalidadesSelecionadas.joinToString { id ->
                                listaModalidades.find { it.id == id }?.nomeModalidade ?: "ID $id"
                            },
                            color = colorScheme.primary
                        )
                    }

                    DropdownMenu(
                        expanded = showModalidadesMenu,
                        onDismissRequest = { showModalidadesMenu = false },
                        tonalElevation = 0.dp, // remove sombra escura
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // Define 90% da largura do pai
                            .background(colorScheme.primaryContainer) // aplica ao menu inteiro

                    ) {
                        listaModalidades.forEach { modalidade ->
                            DropdownMenuItem(
                                onClick = {
                                    if (modalidadesSelecionadas.contains(modalidade.id)) {
                                        modalidadesSelecionadas.remove(modalidade.id)
                                    } else {
                                        modalidadesSelecionadas.add(modalidade.id)
                                    }
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Checkbox(
                                            checked = modalidadesSelecionadas.contains(modalidade.id),
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = colorScheme.primary,
                                                uncheckedColor = colorScheme.secondary
                                            )
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(modalidade.nomeModalidade, color = colorScheme.primary)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        when {
                            nome.isBlank() -> {
                                popupMessage = "Preencha o nome."
                                showPopup = true
                            }

                            password.isBlank() -> {
                                popupMessage = "Preencha a senha."
                                showPopup = true
                            }

                            modalidadesSelecionadas.isEmpty() -> {
                                popupMessage = "Selecione pelo menos uma modalidade."
                                showPopup = true
                            }

                            else -> {
                                viewModel.criarAtleta(
                                    UserCreate(password, nome, "atleta", modalidadesSelecionadas)
                                ) { sucesso, resposta ->
                                    if (sucesso) {
                                        successMessage = resposta
                                        showSuccessPopup = true

                                        nome = ""
                                        password = ""
                                        modalidadesSelecionadas.clear()
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
                    Text("Criar Atleta", color = colorScheme.background)
                }

                Spacer(Modifier.height(24.dp))
                HorizontalDivider(color = colorScheme.secondary)
                Text(
                    "Todos os Atletas",
                    color = colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 16.dp)
                ) {
                    items(atletas) { atleta ->
                        AtletaItem(
                            atleta = atleta,
                            onDeleteClick = {
                                atletaParaApagar = atleta
                                showPasswordDialog = true
                            }
                        )
                    }
                }

                PasswordConfirmDialog(
                    showDialog = showPasswordDialog,
                    descricao = "Tens a certeza que queres eliminar o atleta: ${atletaParaApagar?.nome} (ID: ${atletaParaApagar?.idSocio})?",
                    onDismiss = {
                        showPasswordDialog = false
                        atletaParaApagar = null
                    },
                    onConfirm = { password ->
                        atletaParaApagar?.let { atleta ->
                            viewModel.apagarAtleta(
                                idAtleta = atleta.idSocio,
                                idProfessor = user.idSocio,
                                password = password
                            ) { sucesso, resposta ->

                                // Substitui a mensagem técnica se for erro 401 (não autorizado)
                                val mensagemFinal = if (!sucesso && resposta.contains("401")) {
                                    "Password inválida"
                                } else {
                                    resposta
                                }

                                toastMessage = mensagemFinal
                                isToastSuccess = sucesso
                                showToast = true

                                if (sucesso) {
                                    viewModel.carregarAtletas()
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

