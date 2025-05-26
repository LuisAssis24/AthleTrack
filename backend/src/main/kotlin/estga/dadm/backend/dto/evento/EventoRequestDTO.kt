package estga.dadm.backend.dto.evento

/**
 * DTO para requisição de listagem de eventos por sócio.
 */
data class EventoRequestDTO (
    val idSocio: Int // ID do sócio para filtrar os eventos
)