package estga.dadm.athletrack.screens.professor

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserCreate
import estga.dadm.athletrack.api.UserRequest
import estga.dadm.athletrack.viewmodels.GestaoAtletaViewModel
import kotlinx.coroutines.launch
import android.widget.Toast

@Composable
fun GestaoAtletasScreen(user: User, navController: NavHostController) {
    val viewModel: GestaoAtletaViewModel = viewModel()
    val atletas by viewModel.atletas.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var nome by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val modalidadesSelecionadas = remember { mutableStateListOf<Int>() }
    var showModalidadesMenu by remember { mutableStateOf(false) }

    var showPasswordDialog by remember { mutableStateOf(false) }
    var senhaParaApagar by remember { mutableStateOf("") }
    var atletaParaApagar by remember { mutableStateOf<User?>(null) }

    val listaModalidades = listOf(
        Modalidade(1, "Futebol"),
        Modalidade(2, "Natação"),
        Modalidade(3, "Basquetebol")
    )

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
                    Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.width(8.dp))
                Text("Gestão de Atletas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
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
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(Modifier.height(8.dp))

            Text("Modalidades", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { showModalidadesMenu = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = if (modalidadesSelecionadas.isEmpty()) "Selecionar Modalidades" else modalidadesSelecionadas.joinToString { id ->
                            listaModalidades.find { it.id == id }?.nomeModalidade ?: "ID $id"
                        },
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                DropdownMenu(
                    expanded = showModalidadesMenu,
                    onDismissRequest = { showModalidadesMenu = false },
                    modifier = Modifier.fillMaxWidth()
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
                                            checkedColor = MaterialTheme.colorScheme.primary,
                                            uncheckedColor = MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(modalidade.nomeModalidade, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    if (nome.isBlank() || password.isBlank() || modalidadesSelecionadas.isEmpty()) {
                        coroutineScope.launch {
                            Toast.makeText(context, "Preencha nome, senha e selecione pelo menos uma modalidade.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        viewModel.criarAtleta(
                            UserCreate(password, nome, "atleta", modalidadesSelecionadas)
                        ) { sucesso, resposta ->
                            coroutineScope.launch {
                                Toast.makeText(context, resposta, Toast.LENGTH_LONG).show()
                            }
                            if (sucesso) {
                                nome = ""
                                password = ""
                                modalidadesSelecionadas.clear()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Criar Atleta", color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))
            Divider(color = MaterialTheme.colorScheme.secondary)
            Text("Todos os Atletas", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))

            LazyColumn(modifier = Modifier.weight(1f).padding(bottom = 16.dp)) {
                items(atletas) { atleta ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(atleta.nome, color = Color.White)
                            Text("ID: ${atleta.idSocio}", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                        }
                        IconButton(onClick = {
                            atletaParaApagar = atleta
                            showPasswordDialog = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Apagar", tint = MaterialTheme.colorScheme.error)
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
                                coroutineScope.launch {
                                    Toast.makeText(context, resposta, Toast.LENGTH_LONG).show()
                                }
                                if (sucesso) viewModel.carregarAtletas()
                            }
                            showPasswordDialog = false
                            senhaParaApagar = ""
                            atletaParaApagar = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.background)
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
                        Text("Cancelar", color = MaterialTheme.colorScheme.primary)
                    }
                },
                title = {
                    Text("Confirmar eliminação", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text("Tens a certeza que queres eliminar o atleta: ${atletaParaApagar!!.nome} (ID: ${atletaParaApagar!!.idSocio})?",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = senhaParaApagar,
                            onValueChange = { senhaParaApagar = it },
                            label = { Text("Insere a tua senha", color = MaterialTheme.colorScheme.secondary) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}



