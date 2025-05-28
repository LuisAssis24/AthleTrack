package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de exclusão de treino.
 *
 * Utilizado para transferir os dados necessários para validar e apagar um treino.
 *
 * @property qrCode QR code do treino a ser apagado.
 * @property idSocio ID do professor solicitante da exclusão.
 * @property password Senha do professor para validação da operação.
 */
data class TreinoApagarRequestDTO (
    val qrCode: String,     // QR code do treino a ser apagado
    val idSocio: Int,       // ID do professor solicitante
    val password: String    // Senha do professor para validação
)