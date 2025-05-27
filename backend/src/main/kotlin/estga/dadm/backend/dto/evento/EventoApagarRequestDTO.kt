package estga.dadm.backend.dto.evento

data class EventoApagarRequestDTO(
    val id: Int,
    val idProfessor: Int,
    val password: String
)
