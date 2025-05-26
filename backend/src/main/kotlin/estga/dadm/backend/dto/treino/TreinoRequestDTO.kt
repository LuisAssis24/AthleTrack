package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de listagem de treinos.
 * Utilizado para filtrar treinos por sócio e dia da semana.
 */
data class TreinoRequestDTO(
    val idSocio: Int,       // ID do sócio (professor ou aluno)
    val diaSemana: String   // Dia da semana para filtrar os treinos
)