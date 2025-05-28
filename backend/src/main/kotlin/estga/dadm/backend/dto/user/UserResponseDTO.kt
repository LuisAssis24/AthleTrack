package estga.dadm.backend.dto.user

/**
 * DTO de resposta para operações relacionadas ao usuário.
 *
 * Utilizado para transferir as informações principais do usuário em respostas de API.
 *
 * @property idSocio ID do sócio/usuário.
 * @property nome Nome do usuário.
 * @property tipo Tipo do usuário (ex: professor, atleta).
 */
data class UserResponseDTO(
    val idSocio: Int,   // ID do sócio/usuário
    val nome: String,   // Nome do usuário
    val tipo: String    // Tipo do usuário (ex: professor, atleta)
)