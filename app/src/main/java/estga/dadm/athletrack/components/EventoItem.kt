package estga.dadm.athletrack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import estga.dadm.athletrack.api.Evento
import java.time.LocalDate
import java.util.Locale
import java.time.format.TextStyle

/**
 * Componente que representa um item de evento na interface.
 *
 * @param selectedDate Data selecionada para exibição.
 * @param evento Instância do objeto `Evento` que contém os dados do evento.
 * @param onDetailsClick Função de callback executada ao clicar no botão de detalhes.
 * @param onDeleteClick Função de callback executada ao clicar no botão de apagar.
 */
@Composable
fun EventoItem(
    selectedDate: LocalDate,
    evento: Evento,
    onDetailsClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // Preenche toda a largura disponível.
            .padding(vertical = 8.dp) // Adiciona espaçamento vertical.
            .background(
                colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp) // Define bordas arredondadas.
            )
            .padding(16.dp), // Adiciona espaçamento interno.
        verticalAlignment = Alignment.CenterVertically, // Alinha os itens verticalmente ao centro.
        horizontalArrangement = Arrangement.SpaceBetween // Distribui os itens horizontalmente com espaço entre eles.
    ) {
        // Exibição da data ao lado esquerdo.
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Column(
                    modifier = Modifier.padding(top = 3.dp), // Adiciona espaçamento acima da data.
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Exibe o dia do mês.
                    Text(
                        "${selectedDate.dayOfMonth}",
                        style = typography.titleMedium,
                        color = colorScheme.primary
                    )
                    // Exibe o mês com estilo abreviado e em maiúsculas.
                    Text(
                        selectedDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale("pt", "PT")
                        ).replaceFirstChar { it.uppercase() },
                        style = typography.labelSmall,
                        color = colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp)) // Adiciona espaçamento horizontal.

                Column {
                    // Exibe o local do evento.
                    Text(evento.localEvento, color = colorScheme.primary)
                    // Exibe a hora do evento no formato "hh:mm".
                    Text(
                        evento.hora.take(5),
                        style = typography.labelSmall,
                        color = colorScheme.secondary
                    )
                }
            }
        }
        Row {
            // Botão para exibir detalhes do evento.
            IconButton(onClick = onDetailsClick) {
                Icon(
                    imageVector = Icons.Default.AddCircle, // Ícone de adicionar.
                    contentDescription = "Detalhes", // Descrição do ícone para acessibilidade.
                    tint = colorScheme.primary // Define a cor do ícone.
                )
            }
            // Botão para apagar o evento.
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete, // Ícone de lixeira.
                    contentDescription = "Eliminar", // Descrição do ícone para acessibilidade.
                    tint = colorScheme.error // Define a cor do ícone como erro (vermelho).
                )
            }
        }
    }
}