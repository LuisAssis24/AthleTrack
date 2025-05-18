package estga.dadm.athletrack.screens.atleta

import android.R
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import estga.dadm.athletrack.ui.theme.*
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.viewmodels.HomeAtletaViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.components.QrCameraScanner
import androidx.compose.ui.graphics.Color
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
fun HomeScreenAtleta(
    user: User,
    navController: NavHostController,
    viewModel: HomeAtletaViewModel = viewModel()
) {
    val BackgroundBlueDark = Color(0xFF0D1B2A)
    val TopBarBlue = Color(0xFF1B263B)
    val treinos by viewModel.treinos.collectAsState()
    val scope = rememberCoroutineScope()
    var showCameraDialog by remember { mutableStateOf(false) }
    val gson = Gson()

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showCameraDialog = true
        } else {
            Toast.makeText(context, "Permissão da câmara é necessária", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.carregarTreinos(user.idSocio, viewModel.detetarDiaSemana())
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlueDark),
        containerColor = BackgroundBlueDark
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // TOP BAR
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(TopBarBlue, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = White,
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = user.nome,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = White
                            )
                            Text(
                                text = "Sócio nº ${user.idSocio}",
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Botão QR Code
                        IconButton(onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    android.Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    showCameraDialog = true
                                }
                                else -> {
                                    launcher.launch(android.Manifest.permission.CAMERA)
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        VerticalDivider(
                            modifier = Modifier
                                .height(24.dp)
                                .padding(horizontal = 8.dp),
                            color = Color.LightGray,
                            thickness = 1.dp
                        )

                        // Botão Calendário - redireciona para a tela de calendário
                        IconButton(onClick = {
                            val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                            navController.navigate("calendar/$userJson")
                        }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendário",
                                tint = White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Próximos Treinos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.5.dp,
                color = Color.LightGray
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 24.dp)
            ) {
                if (treinos.isEmpty()) {
                    Text(
                        text = "Não tem treinos agendados para hoje.",
                        textAlign = TextAlign.Center,
                        color = White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                    )
                } else {
                    treinos.forEach {
                        Text(
                            "${it.nomeModalidade} - ${it.diaSemana} - ${it.hora}",
                            color = White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                        )
                        HorizontalDivider(color = Color.LightGray)
                    }
                }
            }

            // CÂMERA QR DIALOG
            if (showCameraDialog) {
                AlertDialog(
                    onDismissRequest = { showCameraDialog = false },
                    confirmButton = {},
                    title = { Text("Ler QR Code") },
                    text = {
                        QrCameraScanner(onCodeScanned = { codigo ->
                            scope.launch {
                                try {
                                    val response = viewModel.apiPresencas.registarPresenca(
                                        PresencaRequest(user.idSocio, codigo)
                                    )
                                    Toast.makeText(context, response.mensagem, Toast.LENGTH_LONG).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    showCameraDialog = false
                                }
                            }
                        })
                    }
                )
            }
        }
    }
}
