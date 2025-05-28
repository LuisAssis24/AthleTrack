package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.ui.theme.*

/**
 * Componente que representa um item de atleta na interface.
 *
 * @param atleta Instância do objeto `User` que contém os dados do atleta.
 * @param onDeleteClick Função de callback executada ao clicar no botão de apagar.
 */
@Composable
fun AtletaItem(
    atleta: User,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Preenche toda a largura disponível.
            .padding(vertical = 8.dp) // Adiciona espaçamento vertical.
            .background(
                colorScheme.primaryContainer,
                RoundedCornerShape(12.dp)
            ) // Define o fundo com cor e bordas arredondadas.
            .padding(16.dp), // Adiciona espaçamento interno.
        verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente ao centro.
        horizontalArrangement = Arrangement.SpaceBetween // Distribui os itens horizontalmente com espaço entre eles.
    ) {
        Column {
            // Exibe o nome do atleta.
            Text(atleta.nome, color = colorScheme.primary)
            // Exibe o ID do atleta com estilo adicional.
            Text(
                "ID: ${atleta.idSocio}",
                color = colorScheme.primary,
                style = Typography.labelSmall
            )
        }
        // Botão para apagar o atleta.
        IconButton(onClick = onDeleteClick) {
            Icon(
                Icons.Default.Delete, // Ícone de lixeira.
                contentDescription = "Apagar", // Descrição do ícone para acessibilidade.
                tint = colorScheme.error // Define a cor do ícone como erro (vermelho).
            )
        }
    }
}