package estga.dadm.backend.dto.user

/**
 * DTO para requisição de login do usuário.
 * Contém o ID do sócio/usuário e a senha.
 */
data class LoginRequestDTO(
    val idSocio: Int,    // ID do sócio/usuário
    val password: String // Senha do usuário
)