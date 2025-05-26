package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para operações de presença.
 * Indica se a operação foi bem-sucedida e uma mensagem associada.
 */
data class PresencaResponseDTO(
    val sucesso: Boolean,   // Indica se a operação foi bem-sucedida
    val mensagem: String    // Mensagem de retorno
)