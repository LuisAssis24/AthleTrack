package estga.dadm.backend.dto.evento

/**
 * DTO utilizado para requisições de listagem de eventos por sócio.
 *
 * @property idSocio Identificador do sócio para filtrar os eventos.
 */
data class EventoRequestDTO(
    val idSocio: Int // ID do sócio para filtrar os eventos
)