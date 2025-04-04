package estga.dadm.athletrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons // Importação dos Icones da google
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.text.style.TextAlign
import estga.dadm.athletrack.ui.theme.*

@Composable
fun HomeScreen(userName: String) {
    val treinos = listOf(
        "Treino Cardio - 02/04",
        "Treino Força - 04/04",
        "Treino Equipa - 06/04",
        "Ginásio Livre - 08/04",
        "Recuperação Ativa - 10/04",
        "Treino Extra - 11/04",
        "Treino Técnico - 12/04",
        "Treino Livre - 13/04",
        "Treino Tático - 14/04",
        "Avaliação Física - 15/04"
    ).take(10)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = White,
                    modifier = Modifier.size(36.dp, 64.dp) // Regula o tamanho do ícone
                )
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Calendário",
                    tint = White,
                    modifier = Modifier.size(36.dp) // Regula o tamanho do ícone
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .wrapContentSize(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp)) // aumentado

            Text("Bem vindo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)

            Spacer(modifier = Modifier.height(40.dp)) // mais espaço entre "bem vindo" e os treinos

            Text("Próximos Treinos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = GrayNeutral
            )

            treinos.forEach {
                Text(
                    it,
                    fontSize = 16.sp,
                    color = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(thickness = 0.5.dp, color = GrayNeutral)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_camera),
                        contentDescription = "Câmara",
                        tint = White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Registar Presença", fontSize = 12.sp, color = White)
            }
        }
    }
}
