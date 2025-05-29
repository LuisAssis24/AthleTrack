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

/**
 * Componente que representa a barra superior da interface.
 *
 * @param user Instância do objeto `User` que contém os dados do usuário atual.
 * @param navController Controlador de navegação para gerenciar as transições entre telas.
 * @param onBottomSheet Função de callback executada ao clicar no botão de perfil.
 * @param qrCodeClick Função de callback executada ao clicar no botão de QR Code.
 */
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
            .fillMaxWidth() // Preenche toda a largura disponível.
            .padding(top = 16.dp) // Adiciona espaçamento superior.
            .background(
                colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) // Define o fundo e bordas arredondadas.
            .padding(16.dp) // Adiciona espaçamento interno.
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Distribui os itens horizontalmente com espaço entre eles.
            verticalAlignment = Alignment.CenterVertically // Alinha os itens verticalmente ao centro.
        ) {
            // LADO ESQUERDO: PERFIL + NOME
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBottomSheet) {
                    Icon(
                        imageVector = Icons.Default.Person, // Ícone de perfil.
                        contentDescription = "Perfil", // Descrição do ícone para acessibilidade.
                        tint = colorScheme.primary, // Define a cor do ícone.
                        modifier = Modifier.size(48.dp) // Define o tamanho do ícone.
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Adiciona espaçamento horizontal.

                Column {
                    // Exibe o nome do usuário.
                    Text(
                        text = user.nome,
                        style = Typography.titleMedium,
                        color = colorScheme.primary
                    )
                    // Exibe o número de sócio do usuário.
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
                            imageVector = Icons.Default.QrCode, // Ícone de QR Code.
                            contentDescription = "QR Code", // Descrição do ícone para acessibilidade.
                            tint = colorScheme.primary, // Define a cor do ícone.
                            modifier = Modifier.size(28.dp) // Define o tamanho do ícone.
                        )
                    }
                }

                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp) // Define a altura do divisor.
                        .padding(horizontal = 8.dp), // Adiciona espaçamento horizontal.
                    color = colorScheme.secondary, // Define a cor do divisor.
                    thickness = 1.dp // Define a espessura do divisor.
                )

                // Botão Calendário - redireciona para a tela de calendário.
                IconButton(onClick = {
                    val userJson = URLEncoder.encode(
                        gson.toJson(user),
                        "UTF-8"
                    ) // Codifica os dados do usuário em JSON.
                    navController.navigate("calendar/$userJson") // Navega para a tela de calendário.
                }) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth, // Ícone de calendário.
                        contentDescription = "Calendário", // Descrição do ícone para acessibilidade.
                        tint = colorScheme.primary, // Define a cor do ícone.
                        modifier = Modifier.size(28.dp) // Define o tamanho do ícone.
                    )
                }
            }
        }
    }

    if (user.tipo == "professor") {
        Spacer(modifier = Modifier.height(24.dp)) // Adiciona espaçamento vertical.

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Distribui os itens horizontalmente de forma uniforme.
        ) {
            Button(
                onClick = {
                    val gson = Gson()
                    val userJson = URLEncoder.encode(
                        gson.toJson(user),
                        "UTF-8"
                    ) // Codifica os dados do usuário em JSON.
                    navController.navigate("gestaotreinos/$userJson") // Navega para a tela de gestão de treinos.
                },
                modifier = Modifier
                    .weight(1f) // Define o peso do botão na distribuição horizontal.
                    .background(
                        colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp) // Define bordas arredondadas.
                    )
                    .size(81.dp), // Define o tamanho do botão.
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.tertiary) // Define as cores do botão.
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.DirectionsBike, // Ícone de bicicleta.
                    contentDescription = "GestaoTreinos", // Descrição do ícone para acessibilidade.
                    modifier = Modifier.size(40.dp), // Define o tamanho do ícone.
                    tint = colorScheme.primary // Define a cor do ícone.
                )
                Spacer(modifier = Modifier.height(4.dp)) // Adiciona espaçamento vertical.
                Text(
                    text = "Gestão de Treinos",
                    style = getAdjustedTypography(), // Aplica tipografia ajustada.
                    textAlign = TextAlign.Center, // Alinha o texto ao centro.
                    color = colorScheme.primary // Define a cor do texto.
                )
            }

            Spacer(modifier = Modifier.width(16.dp)) // Adiciona espaçamento horizontal.

            Button(
                onClick = {
                    val gson = Gson()
                    val userJson = URLEncoder.encode(
                        gson.toJson(user),
                        "UTF-8"
                    ) // Codifica os dados do usuário em JSON.
                    navController.navigate("gestaoatletas/$userJson") // Navega para a tela de gestão de atletas.
                },
                modifier = Modifier
                    .weight(1f) // Define o peso do botão na distribuição horizontal.
                    .background(
                        colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp) // Define bordas arredondadas.
                    )
                    .size(81.dp), // Define o tamanho do botão.
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent) // Define as cores do botão.
            ) {
                Icon(
                    imageVector = Icons.Default.Edit, // Ícone de edição.
                    contentDescription = "GestaoAtleta", // Descrição do ícone para acessibilidade.
                    modifier = Modifier.size(40.dp), // Define o tamanho do ícone.
                    tint = colorScheme.primary // Define a cor do ícone.
                )
                Spacer(modifier = Modifier.height(4.dp)) // Adiciona espaçamento vertical.
                Text(
                    text = "Gestão de Atletas",
                    style = getAdjustedTypography(), // Aplica tipografia ajustada.
                    textAlign = TextAlign.Center, // Alinha o texto ao centro.
                    color = colorScheme.primary // Define a cor do texto.
                )
            }
        }
    } else {
        Spacer(modifier = Modifier.height(24.dp)) // Adiciona espaçamento vertical.
    }
}