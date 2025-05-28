package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de registro de presença.
 *
 * Utilizado para registrar a presença de um sócio em um treino, seja via QR Code ou manualmente.
 *
 * @property idSocio Identificador único do sócio.
 * @property qrCode Código QR do treino utilizado para validação da presença.
 * @property estado Estado da presença (true = presente, false = ausente).
 */
data class PresencaRequestDTO(
    val idSocio: Int,       // ID do sócio
    val qrCode: String,     // Código QR do treino
    val estado: Boolean     // Estado da presença (true = presente)
)