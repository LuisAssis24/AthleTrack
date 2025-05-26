package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para listagem de treinos de um professor.
 */
data class TreinoProfResponseDTO(
    val idTreino: Int,          // ID do treino
    val nomeModalidade: String, // Nome da modalidade
    val diaSemana: String,      // Dia da semana do treino
    val hora: String,           // Hora do treino
    val qrCode: String          // QR Code do treino
)