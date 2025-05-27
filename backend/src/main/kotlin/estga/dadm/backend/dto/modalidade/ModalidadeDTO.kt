package estga.dadm.backend.dto.modalidade

/**
 * DTO que representa uma modalidade.
 * Utilizado para transferir dados de modalidade entre as camadas da aplicação.
 */
data class ModalidadeDTO(
    val id: Int,                  // ‘ID’ da modalidade
    val nomeModalidade: String    // Nome da modalidade
)