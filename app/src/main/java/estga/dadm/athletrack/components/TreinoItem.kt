package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import estga.dadm.athletrack.api.Treino

/**
 * Componente que representa um item de treino na interface.
 *
 * @param treino Instância do objeto `Treino` que contém os dados do treino a ser exibido.
 * @param onDeleteClick Função de callback executada ao clicar no botão de apagar.
 */
@Composable
fun TreinoItem(
    treino: Treino,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Preenche toda a largura disponível.
            .padding(vertical = 8.dp) // Adiciona espaçamento vertical.
            .background(
                colorScheme.primaryContainer, // Define a cor de fundo do item.
                shape = RoundedCornerShape(12.dp) // Define bordas arredondadas.
            )
            .padding(16.dp), // Adiciona espaçamento interno.
        verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente ao centro.
        horizontalArrangement = Arrangement.SpaceBetween // Distribui os itens horizontalmente com espaço entre eles.
    ) {
        Column {
            // Exibe o nome da modalidade, dia da semana e hora do treino.
            Text(
                "${treino.nomeModalidade} - ${treino.diaSemana} ${treino.hora}",
                color = colorScheme.primary // Define a cor do texto.
            )
            // Exibe o código QR associado ao treino.
            Text(
                "QR: ${treino.qrCode}",
                fontSize = 12.sp, // Define o tamanho da fonte.
                color = colorScheme.primary, // Define a cor do texto.
            )
        }
        // Botão para apagar o treino.
        IconButton(onClick = onDeleteClick) {
            Icon(
                Icons.Default.Delete, // Ícone de apagar.
                contentDescription = "Apagar", // Descrição do ícone para acessibilidade.
                tint = colorScheme.error // Define a cor do ícone.
            )
        }
    }
}