package estga.dadm.backend.dto.user

/**
 * DTO de resposta para operações relacionadas ao usuário.
 * Contém as informações principais do usuário.
 */
data class UserResponseDTO(
    val idSocio: Int,   // ID do sócio/usuário
    val nome: String,   // Nome do usuário
    val tipo: String    // Tipo do usuário (ex: professor, atleta)
)