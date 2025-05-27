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

@Composable
fun EventoItem(
    selectedDate: LocalDate,
    evento: Evento,
    onDetailsClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Data ao lado esquerdo
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Column(
                    modifier = Modifier.padding(top = 3.dp), // Adiciona espaçamento acima da data
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${selectedDate.dayOfMonth}",
                        style = typography.titleMedium,
                        color = colorScheme.primary
                    )
                    Text(
                        selectedDate.month.getDisplayName(
                            TextStyle.SHORT,
                            Locale("pt", "PT")
                        ).replaceFirstChar { it.uppercase() },
                        style = typography.labelSmall,
                        color = colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(evento.localEvento, color = colorScheme.primary)
                    Text(
                        evento.hora.take(5), // Apenas "hh:mm"
                        style = typography.labelSmall,
                        color = colorScheme.secondary
                    )
                }
            }
        }
        Row {
            // Ações à direita
            IconButton(onClick = onDetailsClick) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Detalhes",
                    tint = colorScheme.primary,
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = colorScheme.error
                )
            }
        }

    }
}
