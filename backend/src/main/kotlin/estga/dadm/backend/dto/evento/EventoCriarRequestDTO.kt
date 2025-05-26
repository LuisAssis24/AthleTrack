package estga.dadm.backend.dto.evento

/**
 * DTO para requisição de criação de evento.
 * Recebe os dados necessários para criar um novo evento e associá-lo a modalidades.
 */
data class EventoCriarRequestDTO(
    val data: String,                // Data do evento (formato String)
    val hora: String,                // Hora do evento (formato String)
    val localEvento: String,         // Local onde o evento será realizado
    val descricao: String,           // Descrição do evento
    val modalidades: List<Int>,      // Lista de IDs das modalidades associadas ao evento
)