package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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
import estga.dadm.athletrack.api.UserCreate
import estga.dadm.athletrack.viewmodels.GestaoAtletaViewModel

@Composable
fun GestaoAtletasScreen(user: User, navController: NavHostController) {
    val viewModel: GestaoAtletaViewModel = viewModel()
    val atletas by viewModel.atletas.collectAsState()

    var nome by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var modalidades by remember { mutableStateOf("") }
    var mensagem by remember { mutableStateOf("") }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var senhaParaApagar by remember { mutableStateOf("") }
    var atletaParaApagar by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        viewModel.carregarAtletas()
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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Text("Gestão de Atletas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        containerColor = Color(0xFF001F3F)
    ) { padding ->
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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = modalidades,
                onValueChange = { modalidades = it },
                label = { Text("IDs Modalidades (ex: 1,2)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    val ids = modalidades.split(",").mapNotNull { it.trim().toIntOrNull() }
                    viewModel.criarAtleta(
                        UserCreate(password, nome, "atleta", ids)
                    ) { sucesso, resposta ->
                        mensagem = resposta
                        if (sucesso) {
                            nome = ""
                            password = ""
                            modalidades = ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4))
            ) {
                Text("Criar Atleta", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (mensagem.isNotEmpty()) {
                Text(text = mensagem, color = Color.LightGray, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(Modifier.height(24.dp))
            Divider(color = Color.Gray)
            Text("Todos os Atletas", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 16.dp)) {
                items(atletas) { atleta ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(atleta.nome, color = Color.White)
                            Text("ID: ${atleta.idSocio}", color = Color.LightGray, fontSize = 12.sp)
                        }
                        IconButton(onClick = {
                            atletaParaApagar = atleta
                            showPasswordDialog = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = Color.Red)
                        }
                    }
                }
            }
        }

        if (showPasswordDialog && atletaParaApagar != null) {
            AlertDialog(
                onDismissRequest = {
                    showPasswordDialog = false
                    senhaParaApagar = ""
                    atletaParaApagar = null
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.apagarAtletaComSenha(
                                idAtleta = atletaParaApagar!!.idSocio,
                                idProfessor = user.idSocio,
                                senha = senhaParaApagar
                            ) { sucesso, resposta ->
                                mensagem = resposta
                                if (sucesso) viewModel.carregarAtletas()
                            }
                            showPasswordDialog = false
                            senhaParaApagar = ""
                            atletaParaApagar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe53935))
                    ) {
                        Text("Eliminar", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showPasswordDialog = false
                            senhaParaApagar = ""
                            atletaParaApagar = null
                        }
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                },
                title = {
                    Text("Confirmar eliminação", color = Color.White, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text("Tens a certeza que queres eliminar o atleta: ${atletaParaApagar!!.nome} (ID: ${atletaParaApagar!!.idSocio})?",
                            color = Color.White,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = senhaParaApagar,
                            onValueChange = { senhaParaApagar = it },
                            label = { Text("Insere a tua senha", color = Color.LightGray) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                containerColor = Color(0xFF121212)
            )
        }
    }
}



