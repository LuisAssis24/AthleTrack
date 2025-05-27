package estga.dadm.backend.dto.evento

import java.time.LocalDate
import java.time.LocalTime

/**
 * DTO de resposta para listagem de eventos.
 * Contém as informações principais de um evento.
 */
data class EventoResponseDTO (
    val id: Int,              // ID do evento
    val localEvento: String,     // Local onde o evento será realizado
    val data: LocalDate,         // Data do evento
    val hora: LocalTime,         // Hora do evento
    val descricao: String,       // Descrição do evento
)