package estga.dadm.backend.dto.user

/**
 * DTO para requisição de exclusão de usuário.
 *
 * Utilizado para transferir o ID do sócio/usuário que será excluído do sistema.
 *
 * @property idSocio ID do sócio/usuário a ser excluído.
 */
data class UserDeleteRequestDTO(
    val idSocio: Int,    // ID do sócio/usuário a ser excluído
)