package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de exclusão de treino.
 * Contém informações necessárias para validar e apagar um treino.
 */
data class TreinoApagarRequestDTO (
    val qrCode: String,     // QR code do treino a ser apagado
    val idSocio: Int,       // ID do professor solicitante
    val password: String    // Senha do professor para validação
)