package estga.dadm.athletrack.partials

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import estga.dadm.athletrack.api.Evento
import estga.dadm.athletrack.ui.theme.BackgroundBlueDark
import java.util.Calendar

@Composable
fun EventDetailsDialog(
    evento: Evento,
    onDismiss: () -> Unit,
    context: Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                adicionarEventoAoCalendario(context, evento)
                onDismiss()
            }) {
                Text("Adicionar ao Calendário")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                "Detalhes do Evento",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Nome:", color = Color.White)
                Text(evento.localEvento, fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Data e Hora:", color = Color.White)
                Text("${evento.data} ${evento.hora}", fontWeight = FontWeight.Bold, color = Color.White)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Descrição:", color = Color.White)
                Text(
                    text = if (evento.descricao.isNullOrEmpty()) "Sem descrição" else evento.descricao,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        containerColor = BackgroundBlueDark,
        shape = RoundedCornerShape(16.dp)
    )
}

fun adicionarEventoAoCalendario(context: Context, evento: Evento) {
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, evento.localEvento)
        putExtra(CalendarContract.Events.DESCRIPTION, evento.descricao)

        val startMillis = Calendar.getInstance().apply {
            set(Calendar.YEAR, evento.data.substring(0,4).toInt())
            set(Calendar.MONTH, evento.data.substring(5,7).toInt() - 1)
            set(Calendar.DAY_OF_MONTH, evento.data.substring(8,10).toInt())
            set(Calendar.HOUR_OF_DAY, evento.hora.substring(0,2).toInt())
            set(Calendar.MINUTE, evento.hora.substring(3,5).toInt())
        }.timeInMillis


        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startMillis + 60 * 60 * 1000)
    }

    context.startActivity(intent)
}
