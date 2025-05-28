package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de criação de treino.
 *
 * Utilizado para transferir os dados necessários para criar um novo treino.
 *
 * @property diaSemana Dia da semana em que o treino ocorrerá.
 * @property hora Horário do treino.
 * @property idModalidade Identificador da modalidade do treino.
 * @property idProfessor Identificador do professor responsável pelo treino.
 */
data class TreinoCriarRequestDTO(
    val diaSemana: String,      // Dia da semana do treino
    val hora: String,           // Hora do treino
    val idModalidade: Int,      // ID da modalidade do treino
    val idProfessor: Int        // ID do professor responsável
)