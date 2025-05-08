package estga.dadm.backend.dto

import java.time.LocalDate
import java.time.LocalTime

data class EventoResponseDTO (
    val localEvento: String,
    val data: LocalDate,
    val hora: LocalTime,
    val descricao: String,
)