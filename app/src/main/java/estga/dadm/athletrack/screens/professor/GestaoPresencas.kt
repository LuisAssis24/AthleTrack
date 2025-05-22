package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.api.PresencaListResponseDTO
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.GestaoPresencasViewModel

/**
 *
 */
@Composable
fun GestaoPresencas(
    user: User,
    qrCode: String,
    navController: NavHostController,
    viewModel: GestaoPresencasViewModel = viewModel()
) {
    val treinoInfo by viewModel.treinoInfo.collectAsState()
    val atletas by viewModel.alunos.collectAsState()

    // Carregar informações do treino e atletas
    LaunchedEffect(qrCode) {
        viewModel.carregarPresencas(qrCode, user.idSocio)
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Gestão de Presenças",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Exibir informações do treino
            treinoInfo?.let { treino ->
                Text(
                    text = "${treino.nomeModalidade} - ${treino.diaSemana}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Data: ${treino.hora}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Listagem de atletas com checkboxes
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(atletas) { atleta ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = atleta.nome,
                            modifier = Modifier.weight(1f),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Checkbox(
                            checked = atleta.estado,
                            onCheckedChange = { viewModel.atualizarPresenca(atleta.id, it) }
                        )
                    }
                }
            }

            // Botão para salvar presenças
            Button(
                onClick = { viewModel.salvarPresencas(qrCode) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Salvar Presenças",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}