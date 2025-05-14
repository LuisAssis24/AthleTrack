package estga.dadm.backend.dto.treino

data class TreinoCriarRequestDTO(
    val diaSemana: String,
    val hora: String,
    val idModalidade: Int,
    val idProfessor: Int
)
