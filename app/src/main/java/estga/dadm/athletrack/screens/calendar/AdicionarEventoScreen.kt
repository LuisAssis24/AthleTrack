package estga.dadm.athletrack.screens.calendar

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import estga.dadm.athletrack.ui.theme.*
import kotlinx.coroutines.launch
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.AdicionarEventoViewModel
import java.time.LocalDate
import java.time.LocalTime
import android.app.DatePickerDialog
import androidx.compose.material3.MaterialTheme.colorScheme
import estga.dadm.athletrack.api.Modalidade

import java.util.*

@Composable
fun AdicionarEventoScreen(
    user: User,
    navController: NavHostController,
    selectedDate: LocalDate,
    viewModel: AdicionarEventoViewModel = viewModel()
) {
    var data by remember { mutableStateOf(selectedDate) }
    var hora by remember { mutableStateOf(LocalTime.now()) }
    var local by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    val modalidades by viewModel.modalidades.collectAsState()
    val modalidadesSelecionadas = remember { mutableStateListOf<Modalidade>() }
    var isLoading by remember { mutableStateOf(true) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            viewModel.carregarModalidades()
        } catch (e: Exception) {
            println("Erro ao carregar modalidades: ${e.message}")
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Text(
                    text = "Adicionar Evento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
        },
        containerColor = colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = BlueAccent)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Botão para selecionar a data
                Button(
                    onClick = {
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                data = LocalDate.of(year, month + 1, dayOfMonth)
                            },
                            data.year,
                            data.monthValue - 1,
                            data.dayOfMonth
                        )
                        datePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Text("Data: ${data.toString()}", color = White)
                }

                // Botão para selecionar a hora
                Button(
                    onClick = {
                        val timePicker = TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                hora = LocalTime.of(hourOfDay, minute)
                            },
                            hora.hour,
                            hora.minute,
                            true
                        )
                        timePicker.show()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Text("Hora: ${hora.toString()}", color = White)
                }

                OutlinedTextField(
                    value = local,
                    onValueChange = { local = it },
                    label = { Text("Local") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueAccent,
                        unfocusedBorderColor = Gray,
                        focusedLabelColor = BlueAccent,
                        unfocusedLabelColor = White,
                        cursorColor = BlueAccent
                    )
                )

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BlueAccent,
                        unfocusedBorderColor = Gray,
                        focusedLabelColor = BlueAccent,
                        unfocusedLabelColor = White,
                        cursorColor = BlueAccent
                    )
                )

                // MultiSelect para Modalidades
                Text("Modalidades", fontWeight = FontWeight.Bold, color = White)
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { isDropdownExpanded = !isDropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                    ) {
                        Text(
                            text = if (modalidadesSelecionadas.isEmpty()) "Selecionar Modalidades"
                            else modalidadesSelecionadas.joinToString { it.nomeModalidade },
                            color = White
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        modalidades.forEach { modalidade ->
                            DropdownMenuItem(
                                onClick = {
                                    if (modalidade in modalidadesSelecionadas) {
                                        modalidadesSelecionadas.remove(modalidade)
                                    } else {
                                        modalidadesSelecionadas.add(modalidade)
                                    }
                                },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = modalidade in modalidadesSelecionadas,
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = BlueAccent,
                                                uncheckedColor = Gray
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(modalidade.nomeModalidade, color = White)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                viewModel.adicionarEvento(
                                    data = data.toString(),
                                    hora = hora.toString(),
                                    local = local,
                                    descricao = descricao,
                                    modalidades = modalidadesSelecionadas.map { it.id }
                                ) {
                                    // Sucesso: Voltar para a página de calendário e atualizar eventos
                                    navController.popBackStack("calendar/${user.idSocio}", inclusive = false)
                                }
                            } catch (e: Exception) {
                                // Exibir mensagem de erro no Snackbar
                                snackbarHostState.showSnackbar(
                                    message = "Erro ao criar evento: ${e.message}",
                                    actionLabel = "Fechar"
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = BlueAccent)
                ) {
                    Text("Salvar", color = White)
                }
            }
        }
    }
}