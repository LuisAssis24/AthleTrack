package estga.dadm.backend.dto

data class TreinoProfResponseDTO(
    val nomeModalidade: String,
    val diaSemana: String,
    val hora: String,
    val qrCode: String
)