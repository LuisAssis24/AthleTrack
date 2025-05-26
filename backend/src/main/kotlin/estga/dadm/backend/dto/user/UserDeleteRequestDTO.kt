package estga.dadm.backend.dto.user

/**
 * DTO para requisição de exclusão de usuário.
 * Contém o ID do sócio/usuário a ser excluído.
 */
data class UserDeleteRequestDTO(
    val idSocio: Int,    // ID do sócio/usuário a ser excluído
)