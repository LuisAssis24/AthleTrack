package estga.dadm.backend.dto

data class TreinoCriarRequestDTO (
    val diaSemana: String,
    val hora: String,
    val qrCode: String,
    val idModalidade: Int,
    val idProfessor: Int
)