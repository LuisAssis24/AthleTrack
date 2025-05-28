package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.other.getAdjustedTypography
import estga.dadm.athletrack.ui.theme.Typography
import java.net.URLEncoder

@Composable
fun TopBar(
    user: User,
    navController: NavHostController,
    onBottomSheet: () -> Unit,
    qrCodeClick: () -> Unit
) {

    val gson = Gson()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .background(colorScheme.primaryContainer, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // LADO ESQUERDO: PERFIL + NOME
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBottomSheet) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }


                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = user.nome,
                        style = Typography.titleMedium,
                        color = colorScheme.primary
                    )
                    Text(
                        text = "Sócio nº ${user.idSocio}",
                        style = Typography.labelMedium,
                        color = colorScheme.secondary
                    )
                }
            }

            // LADO DIREITO: ÍCONES DE AÇÃO
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (user.tipo == "atleta") {
                    IconButton(onClick = qrCodeClick)
                    {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "QR Code",
                            tint = colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 8.dp),
                    color = colorScheme.secondary,
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
                        tint = colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }

    if (user.tipo == "professor") {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    val gson = Gson()
                    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                    navController.navigate("gestaotreinos/$userJson")
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .size(81.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.tertiary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                    contentDescription = "GestaoTreinos",
                    modifier = Modifier.size(40.dp),
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gestão de Treinos",
                    style = getAdjustedTypography(),
                    textAlign = TextAlign.Center,
                    color = colorScheme.primary
                )
            }


            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    val gson = Gson()
                    val userJson = URLEncoder.encode(gson.toJson(user), "UTF-8")
                    navController.navigate("gestaoatletas/$userJson")
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .size(81.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "GestaoAtleta",
                    modifier = Modifier.size(40.dp),
                    tint = colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gestão de Atletas",
                    style = getAdjustedTypography(),
                    textAlign = TextAlign.Center,
                    color = colorScheme.primary
                )
            }
        }
    } else {
       Spacer(modifier = Modifier.height(48.dp))
    }
}