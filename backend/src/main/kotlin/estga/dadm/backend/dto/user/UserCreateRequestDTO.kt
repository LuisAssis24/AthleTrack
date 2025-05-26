package estga.dadm.backend.dto.user

/**
 * DTO para requisição de criação de usuário.
 * Contém os dados necessários para criar um novo usuário e associá-lo a modalidades.
 */
data class UserCreateRequestDTO(
    val password: String,        // Senha do usuário
    val nome: String,            // Nome do usuário
    val tipo: String,            // Tipo do usuário (ex: professor, atleta)
    val modalidades: List<Int>   // Lista de IDs das modalidades associadas ao usuário
)