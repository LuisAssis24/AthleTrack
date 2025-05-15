package estga.dadm.backend.dto.evento

data class EventoCriarRequestDTO(
    val data: String,
    val hora: String,
    val localEvento: String,
    val descricao: String,
    val modalidades: List<Int>,
)