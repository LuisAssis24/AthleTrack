package estga.dadm.backend.dto.treino

/**
 * DTO de resposta para operações de presença.
 *
 * Indica se a operação de registro de presença foi bem-sucedida e retorna uma mensagem associada.
 *
 * @property sucesso Indica se a operação foi bem-sucedida.
 * @property mensagem Mensagem de retorno relacionada à operação.
 */
data class PresencaResponseDTO(
    val sucesso: Boolean,   // Indica se a operação foi bem-sucedida
    val mensagem: String    // Mensagem de retorno
)