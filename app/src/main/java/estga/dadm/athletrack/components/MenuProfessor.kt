package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.ui.theme.GrayNeutral
import estga.dadm.athletrack.ui.theme.White

@Composable
fun MenuProfessor(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .background(DarkGray)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(GrayNeutral),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Foto de Perfil",
                tint = White,
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = userName,
            fontSize = 18.sp,
            color = White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Opções específicas do professor
        Button(onClick = {}, colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        )) {
            Text("Avaliações")
        }
        Button(onClick = {}, colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        )) {
            Text("Gestão de Treinos")
        }
        Button(onClick = {}, colors = ButtonDefaults.textButtonColors(
            contentColor = Color.White
        )) {
            Text("Listar Atletas")
        }
    }
}
