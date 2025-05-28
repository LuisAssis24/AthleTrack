package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para listagem de treinos disponíveis para o aluno.
 *
 * Contém informações sobre a modalidade, o dia da semana e o horário do treino.
 *
 * @property nomeModalidade Nome da modalidade do treino.
 * @property diaSemana Dia da semana em que o treino ocorre.
 * @property hora Horário do treino.
 */
data class TreinoAlunoResponseDTO(
    val nomeModalidade: String, // Nome da modalidade do treino
    val diaSemana: String,      // Dia da semana do treino
    val hora: String            // Hora do treino
)