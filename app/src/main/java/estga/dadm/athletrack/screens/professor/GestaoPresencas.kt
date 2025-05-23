package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import estga.dadm.athletrack.ui.theme.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import java.time.format.TextStyle
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
                    "Gestão de Presenças",
                    style = Typography.displayLarge,
                    color = colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Exibir informações do treino
            treinoInfo?.let { treino ->
                Text(
                    text = "${treino.nomeModalidade} - ${treino.diaSemana}",
                    style = Typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Hora: ${treino.hora}",
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Listagem de atletas com estilização
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(atletas) { atleta ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = atleta.nome,
                                style = Typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "ID: ${atleta.id}",
                                style = Typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Checkbox(
                            checked = atleta.estado,
                            onCheckedChange = { viewModel.atualizarPresenca(atleta.id, it) }
                        )
                    }
                }
            }

            // Botão para salvar presenças
            Spacer(modifier = Modifier.height(16.dp))
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