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
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun GestaoAtletasScreen(user: User, navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var modalidades by remember { mutableStateOf("") } // "1,2"
    var mensagem by remember { mutableStateOf("") }

    var atletas by remember { mutableStateOf<List<User>>(emptyList()) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var senhaParaApagar by remember { mutableStateOf("") }
    var atletaParaApagar by remember { mutableStateOf<User?>(null) }

    val scope = rememberCoroutineScope()

    fun carregarAtletas() {
        scope.launch {
            try {
                val todos = RetrofitClient.loginService.listar()
                atletas = todos.filter { it.tipo.lowercase() == "atleta" }
            } catch (e: Exception) {
                mensagem = "Erro ao carregar atletas"
            }
        }
    }

    fun criarAtleta() {
        scope.launch {
            try {
                val ids = modalidades.split(",").mapNotNull { it.trim().toIntOrNull() }
                val req = UserCreate(password, nome, "atleta", ids)
                val resposta = RetrofitClient.loginService.criar(req)
                mensagem = resposta
                nome = ""
                password = ""
                modalidades = ""
                carregarAtletas()
            } catch (e: Exception) {
                mensagem = "Erro ao criar atleta"
            }
        }
    }

    fun apagarAtletaComSenha() {
        val atletaId = atletaParaApagar?.idSocio ?: run {
            mensagem = "Erro: atleta a eliminar não definido"
            return
        }

        scope.launch {
            try {
                val loginRequest = UserRequest(
                    idSocio = user.idSocio,
                    password = senhaParaApagar.trim()
                )

                val resposta = RetrofitClient.loginService.eliminar(
                    idParaEliminar = atletaId,
                    request = loginRequest
                )

                mensagem = resposta
                carregarAtletas()

            } catch (e: Exception) {
                mensagem = "Erro ao apagar: ${e.message}"
            } finally {
                showPasswordDialog = false
                senhaParaApagar = ""
                atletaParaApagar = null
            }
        }
    }

    LaunchedEffect(Unit) {
        carregarAtletas()
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
                onClick = { criarAtleta() },
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
                        onClick = { apagarAtletaComSenha() },
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
                    Text(
                        "Confirmar eliminação",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Tens a certeza que queres eliminar o atleta: ${atletaParaApagar!!.nome} (ID: ${atletaParaApagar!!.idSocio})?",
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



