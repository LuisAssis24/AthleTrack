package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para listagem de treinos disponíveis para o aluno.
 */
data class TreinoAlunoResponseDTO(
    val nomeModalidade: String, // Nome da modalidade do treino
    val diaSemana: String,      // Dia da semana do treino
    val hora: String            // Hora do treino
)