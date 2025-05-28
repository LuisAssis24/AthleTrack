package estga.dadm.athletrack.screens.calendar

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.gson.Gson
import estga.dadm.athletrack.api.Evento
import estga.dadm.athletrack.ui.theme.*
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.other.LoadingScreen
import estga.dadm.athletrack.viewmodels.CalendarViewModel
import estga.dadm.athletrack.partials.PasswordConfirmDialog
import java.net.URLEncoder
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import androidx.compose.ui.platform.LocalContext
import estga.dadm.athletrack.partials.EventDetailsDialog
import estga.dadm.athletrack.components.EventoItem

/**
 * Tela de calendário que exibe eventos e permite interação com as datas.
 *
 * @param user O usuário logado.
 * @param navController Controlador de navegação para gerenciar rotas.
 * @param viewModel ViewModel responsável por gerenciar o estado da tela.
 */
@Composable
fun CalendarScreen(
    user: User,
    navController: NavHostController,
    viewModel: CalendarViewModel = viewModel()
) {
    // Estado da data selecionada
    val selectedDate by viewModel.selectedDate.collectAsState()
    // Lista de eventos carregados
    val eventos by viewModel.eventos.collectAsState()
    // Mês atual exibido no calendário
    val currentMonth by viewModel.currentMonth.collectAsState()

    // Verifica se os eventos estão sendo carregados
    val isLoading = eventos.isEmpty()

    // Carrega eventos para o mês atual ao inicializar ou mudar o mês
    LaunchedEffect(currentMonth) {
        viewModel.carregarEventosParaMes(user.idSocio)
    }

    // Filtra eventos para a data selecionada
    val eventosFiltrados = eventos.filter { evento ->
        val dataEvento = LocalDate.parse(evento.data)
        dataEvento == selectedDate
    }

    // Calcula os dias no mês e o primeiro dia da semana
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7
    val monthLabel = currentMonth.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
        .replaceFirstChar { it.uppercase() }

    // Tela de carregamento enquanto os eventos são carregados
    LoadingScreen(isLoading = isLoading) {
        Scaffold(
            topBar = {
                // Barra superior com botão de voltar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            },
            containerColor = colorScheme.surface
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Título da tela
                Text(
                    "Próximos Eventos",
                    style = typography.displayLarge,
                    color = colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Cartão do calendário
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Navegação entre meses
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                viewModel.irParaMesAnterior()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Mês anterior",
                                    tint = colorScheme.primary
                                )
                            }
                            Text(
                                "$monthLabel ${currentMonth.year}",
                                color = colorScheme.primary
                            )
                            IconButton(onClick = {
                                viewModel.irParaMesSeguinte()
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Próximo mês",
                                    tint = colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Cabeçalho dos dias da semana
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                                Text(
                                    it,
                                    style = Typography.labelSmall,
                                    color = colorScheme.secondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Geração do calendário
                        val totalSlots = firstDayOfWeek + daysInMonth
                        val weeks = (totalSlots + 6) / 7
                        for (week in 0 until weeks) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (dayOfWeek in 0..6) {
                                    val dayIndex = week * 7 + dayOfWeek
                                    val day = dayIndex - firstDayOfWeek + 1

                                    if (day in 1..daysInMonth) {
                                        val date = currentMonth.atDay(day)
                                        val hasEventos = eventos.any { evento ->
                                            LocalDate.parse(evento.data) == date
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (date == selectedDate) colorScheme.secondary else colorScheme.tertiary
                                                )
                                                .clickable {
                                                    viewModel.selecionarData(date)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = "$day",
                                                    style = Typography.labelMedium,
                                                    color = colorScheme.primary
                                                )
                                                if (hasEventos) {
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .clip(CircleShape)
                                                            .background(colorScheme.secondary)
                                                    )
                                                } else {
                                                    Spacer(modifier = Modifier.size(6.dp))
                                                }
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(36.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botão para adicionar evento (apenas para professores)
                        if (user.tipo.lowercase() == "professor") {
                            Button(
                                onClick = {
                                    val userJson = URLEncoder.encode(Gson().toJson(user), "UTF-8")
                                    navController.navigate("adicionarEvento/$userJson/${selectedDate.toString()}")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorScheme.primary,
                                    contentColor = colorScheme.background
                                )
                            ) {
                                Text(
                                    text = "Adicionar Evento",
                                    style = typography.labelMedium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                var showEventDialog by remember { mutableStateOf(false) }
                var eventoParaDetalhes by remember { mutableStateOf<Evento?>(null) }

                var showPasswordDialog by remember { mutableStateOf(false) }
                var eventoSelecionado by remember { mutableStateOf<Evento?>(null) }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        showEventDialog = true
                    } else {
                        //Toast.makeText(context, "Permissão negada para escrever no calendário", Toast.LENGTH_SHORT).show()
                    }
                }

                // Lista de eventos filtrados
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {


                    items(eventosFiltrados) { evento ->
                        EventoItem(
                            selectedDate = selectedDate,
                            evento = evento,
                            onDetailsClick = {
                                eventoParaDetalhes = evento
                                permissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
                            },
                            onDeleteClick = {
                                showPasswordDialog = true
                                eventoSelecionado = evento
                            }
                        )
                    }
                }

                PasswordConfirmDialog(
                    showDialog = showPasswordDialog,
                    descricao = "Tem a certeza que quer eliminar este evento? Esta ação não pode ser desfeita.",
                    onDismiss = {
                        showPasswordDialog = false
                        eventoSelecionado = null
                    },

                    onConfirm = { password ->
                        eventoSelecionado?.let { evento ->
                            viewModel.apagarEvento(
                                idEvento = evento.id,
                                idProfessor = user.idSocio,
                                password = password,
                            ) { sucesso, mensagem ->
                                if (sucesso) {
                                    viewModel.carregarEventosParaMes(user.idSocio)
                                    showPasswordDialog = false
                                }
                            }
                        }
                    }
                )

                eventoParaDetalhes?.let { evento ->
                    EventDetailsDialog(
                        evento = evento,
                        onDismiss = {
                            showEventDialog = false
                            eventoParaDetalhes = null
                        },
                        context = LocalContext.current
                    )
                }

            }
        }
    }
}