package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para listagem de treinos de um professor.
 *
 * Contém informações detalhadas sobre cada treino associado ao professor.
 *
 * @property idTreino Identificador único do treino.
 * @property nomeModalidade Nome da modalidade do treino.
 * @property diaSemana Dia da semana em que o treino ocorre.
 * @property hora Horário do treino.
 * @property qrCode QR Code associado ao treino.
 */
data class TreinoProfResponseDTO(
    val idTreino: Int,          // ID do treino
    val nomeModalidade: String, // Nome da modalidade
    val diaSemana: String,      // Dia da semana do treino
    val hora: String,           // Hora do treino
    val qrCode: String          // QR Code do treino
)