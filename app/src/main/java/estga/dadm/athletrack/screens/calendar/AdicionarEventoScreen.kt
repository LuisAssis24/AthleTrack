package estga.dadm.athletrack.screens.calendar

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.platform.LocalDensity
import com.google.gson.Gson
import estga.dadm.athletrack.api.Modalidade
import java.net.URLEncoder
import estga.dadm.athletrack.ui.theme.*

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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        },
        containerColor = colorScheme.surface,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorScheme.primary)
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
                Text(
                    text = "Adicionar Evento",
                    style = typography.displayLarge,
                    color = colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão para selecionar a data
                OutlinedButton(
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
                    border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                    shape = RoundedCornerShape(4.dp), // Menos arredondado
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.secondary
                    )
                ) {
                    Text("Data: ${data.toString()}")
                }

                // Botão para selecionar a hora
                OutlinedButton(
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
                    border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                    shape = RoundedCornerShape(4.dp), // Menos arredondado
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorScheme.primaryContainer,
                        contentColor = colorScheme.secondary
                    )
                ) {
                    Text("Hora: ${hora.toString()}")
                }

                OutlinedTextField(
                    value = local,
                    onValueChange = { local = it },
                    label = { Text("Local") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.secondary,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.secondary,
                        cursorColor = colorScheme.primary
                    )
                )

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it },
                    label = { Text("Descrição") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp), // Define altura para simular uma área de texto
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.secondary,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.secondary,
                        cursorColor = colorScheme.primary
                    ),
                    maxLines = 5 // Permite múltiplas linhas
                )

                // MultiSelect para Modalidades
                Text("Modalidades", color = colorScheme.primary)
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { isDropdownExpanded = !isDropdownExpanded },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.primaryContainer,
                            contentColor = colorScheme.secondary
                        ),
                        border = BorderStroke(1.dp, colorScheme.onPrimary), // Igual ao TextBox
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (modalidadesSelecionadas.isEmpty()) "Selecionar Modalidades" else modalidadesSelecionadas.joinToString { it.nomeModalidade },
                            color = colorScheme.primary
                        )
                    }

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        tonalElevation = 0.dp, // remove sombra escura
                        modifier = Modifier.fillMaxWidth(0.9f) // Define 80% da largura do pai
                        .background(colorScheme.primaryContainer) // aplica ao menu inteiro
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
                                modifier = Modifier
                                    .fillMaxSize(),
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth() // Garante que o conteúdo ocupe toda a largura
                                    ) {
                                        Checkbox(
                                            checked = modalidade in modalidadesSelecionadas,
                                            onCheckedChange = null,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = colorScheme.primary,
                                                uncheckedColor = colorScheme.secondary
                                            )
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(modalidade.nomeModalidade, color = colorScheme.primary)
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (local.isBlank() || descricao.isBlank() || modalidadesSelecionadas.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Preencha todos os campos obrigatórios",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            coroutineScope.launch {
                                try {
                                    viewModel.adicionarEvento(
                                        idSocio = user.idSocio,
                                        data = data.toString(),
                                        hora = hora.toString(),
                                        local = local,
                                        descricao = descricao,
                                        modalidades = modalidadesSelecionadas.map { it.id },
                                        onSuccess = {
                                            val userJson =
                                                URLEncoder.encode(Gson().toJson(user), "UTF-8")
                                            navController.navigate("calendar/$userJson") {
                                                popUpTo("calendar/$userJson") { inclusive = true }
                                            }
                                        },
                                        onError = { errorMessage ->
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Erro ao criar evento: $errorMessage",
                                                    actionLabel = "Fechar"
                                                )
                                            }
                                        }
                                    )
                                } catch (e: Exception) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Erro ao criar evento: ${e.message}",
                                            actionLabel = "Fechar"
                                        )
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Guardar", color = colorScheme.background)
                }
            }
        }
    }
}