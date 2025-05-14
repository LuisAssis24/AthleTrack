package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel

@Composable
fun GestaoTreinosScreen(user: User) {
    val viewModel: HomeProfessorViewModel = viewModel()
    val treinos by viewModel.treinosTodos.collectAsState()

    var dia by remember { mutableStateOf("SEG") }
    var hora by remember { mutableStateOf("09:00") }
    var modalidadeId by remember { mutableStateOf("1") }

    var mensagem by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.carregarTodosOsTreinos(user.idSocio)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Criar Treino", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = dia, onValueChange = { dia = it }, label = { Text("Dia da Semana") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = hora, onValueChange = { hora = it }, label = { Text("Hora (HH:mm)") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = modalidadeId, onValueChange = { modalidadeId = it }, label = { Text("ID Modalidade") })

        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            viewModel.criarTreino(
                diaSemana = dia,
                hora = hora,
                idModalidade = modalidadeId.toInt(),
                idProfessor = user.idSocio
            ) { sucesso, resposta ->
                mensagem = resposta
                if (sucesso) viewModel.carregarTodosOsTreinos(user.idSocio)
            }
        }) {
            Text("Criar Treino")
        }

        if (mensagem.isNotEmpty()) {
            Text(text = mensagem, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(24.dp))
        Divider()
        Text("Todos os Treinos", fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(treinos) { treino ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("${treino.nomeModalidade} - ${treino.diaSemana} ${treino.hora}")
                        Text("QR: ${treino.qrCode}", fontSize = 12.sp)
                    }
                    IconButton(onClick = {
                        viewModel.apagarTreino(treino.qrCode) {
                            viewModel.carregarTodosOsTreinos(user.idSocio)
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Apagar")
                    }
                }
            }
        }
    }
}
