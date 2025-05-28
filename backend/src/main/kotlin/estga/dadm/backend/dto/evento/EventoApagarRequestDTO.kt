package estga.dadm.backend.dto.evento

/**
 * DTO utilizado para solicitar a exclusão de um evento.
 *
 * @property id Identificador único do evento a ser excluído.
 * @property idProfessor Identificador do professor que solicita a exclusão.
 * @property password Senha do professor para autenticação da solicitação.
 */
data class EventoApagarRequestDTO(
    val id: Int,
    val idProfessor: Int,
    val password: String
)