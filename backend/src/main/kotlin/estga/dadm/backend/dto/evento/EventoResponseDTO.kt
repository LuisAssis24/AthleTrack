package estga.dadm.backend.dto.evento

import java.time.LocalDate
import java.time.LocalTime

/**
 * DTO de resposta para listagem de eventos.
 *
 * Representa os dados principais de um evento retornados em operações de listagem.
 *
 * @property id Identificador único do evento.
 * @property localEvento Local onde o evento será realizado.
 * @property data Data do evento.
 * @property hora Hora do evento.
 * @property descricao Descrição detalhada do evento.
 */
data class EventoResponseDTO(
    val id: Int,              // ID do evento
    val localEvento: String,  // Local onde o evento será realizado
    val data: LocalDate,      // Data do evento
    val hora: LocalTime,      // Hora do evento
    val descricao: String,    // Descrição do evento
)