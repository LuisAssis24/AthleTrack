package estga.dadm.backend.dto.user

/**
 * DTO para requisição de criação de usuário.
 *
 * Utilizado para transferir os dados necessários para criar um novo usuário e associá-lo a modalidades.
 *
 * @property password Senha do usuário.
 * @property nome Nome do usuário.
 * @property tipo Tipo do usuário (ex: professor, atleta).
 * @property modalidades Lista de IDs das modalidades associadas ao usuário.
 */
data class UserCreateRequestDTO(
    val password: String,        // Senha do usuário
    val nome: String,            // Nome do usuário
    val tipo: String,            // Tipo do usuário (ex: professor, atleta)
    val modalidades: List<Int>   // Lista de IDs das modalidades associadas ao usuário
)