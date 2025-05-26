package estga.dadm.backend.dto.treino

/**
 * DTO para requisição de registro de presença.
 * Utilizado tanto para registro via QR Code quanto manual.
 */
data class PresencaRequestDTO(
    val idSocio: Int,       // ID do sócio
    val qrCode: String,     // Código QR do treino
    val estado: Boolean     // Estado da presença (true = presente)
)