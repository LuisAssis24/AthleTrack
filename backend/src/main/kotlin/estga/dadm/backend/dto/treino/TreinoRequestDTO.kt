package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de listagem de treinos.
 *
 * Utilizado para filtrar treinos por sócio (professor ou aluno) e dia da semana.
 *
 * @property idSocio ID do sócio (professor ou aluno) para o filtro.
 * @property diaSemana Dia da semana para filtrar os treinos.
 */
data class TreinoRequestDTO(
    val idSocio: Int,       // ID do sócio (professor ou aluno)
    val diaSemana: String   // Dia da semana para filtrar os treinos
)