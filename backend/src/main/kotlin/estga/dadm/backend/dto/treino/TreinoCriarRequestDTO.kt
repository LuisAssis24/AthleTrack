package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de criação de treino.
 * Contém os dados necessários para criar um novo treino.
 */
data class TreinoCriarRequestDTO(
    val diaSemana: String,      // Dia da semana do treino
    val hora: String,           // Hora do treino
    val idModalidade: Int,      // ID da modalidade do treino
    val idProfessor: Int        // ID do professor responsável
)