package estga.dadm.backend.dto.user

/**
 * DTO para requisição de login do usuário.
 *
 * Utilizado para transferir os dados necessários para autenticação do usuário.
 *
 * @property idSocio ID do sócio/usuário que está tentando fazer login.
 * @property password Senha do usuário para autenticação.
 */
data class LoginRequestDTO(
    val idSocio: Int,    // ID do sócio/usuário
    val password: String // Senha do usuário
)