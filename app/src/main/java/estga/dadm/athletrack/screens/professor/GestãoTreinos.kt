package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel

@Composable
fun GestaoTreinosScreen(user: User, navController: NavHostController) {
    val viewModel: HomeProfessorViewModel = viewModel()
    val treinos by viewModel.treinosTodos.collectAsState()

    var dia by remember { mutableStateOf("SEG") }
    var hora by remember { mutableStateOf("09:00") }
    var modalidadeId by remember { mutableStateOf("1") }

    var mensagem by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.carregarTodosOsTreinos(user.idSocio)
    }

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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Criar Treino",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
            }
        },
        containerColor = colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = dia,
                onValueChange = { dia = it },
                label = { Text("Dia da Semana") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.secondary,
                    cursorColor = colorScheme.primary
                )
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:mm)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.secondary,
                    cursorColor = colorScheme.primary
                )
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = modalidadeId,
                onValueChange = { modalidadeId = it },
                label = { Text("ID Modalidade") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorScheme.primary,
                    unfocusedBorderColor = colorScheme.secondary,
                    cursorColor = colorScheme.primary
                )
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.criarTreino(
                        diaSemana = dia,
                        hora = hora,
                        idModalidade = modalidadeId.toInt(),
                        idProfessor = user.idSocio,
                    ) { sucesso, resposta ->
                        mensagem = resposta
                        if (sucesso) viewModel.carregarTodosOsTreinos(user.idSocio)
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
                Text("Criar Treino", color = colorScheme.background, fontWeight = FontWeight.Bold)
            }

            if (mensagem.isNotEmpty()) {
                Text(
                    text = mensagem,
                    color = if (mensagem.contains("Erro")) colorScheme.error else colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(24.dp))
            Divider(color = colorScheme.secondary)
            Text(
                "Todos os Treinos",
                fontWeight = FontWeight.Bold,
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
                                color = Color.White
                            )
                            Text(
                                "QR: ${treino.qrCode}",
                                fontSize = 12.sp,
                                color = colorScheme.primary,
                            )
                        }
                        IconButton(onClick = {
                            viewModel.apagarTreino(treino.qrCode) {
                                viewModel.carregarTodosOsTreinos(user.idSocio)
                            }
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
}
