package estga.dadm.backend.dto.evento

/**
 * DTO utilizado para requisições de criação de evento.
 *
 * Recebe os dados necessários para criar um novo evento e associá-lo a modalidades específicas.
 *
 * @property data Data do evento (formato String, ex: "2024-06-01").
 * @property hora Hora do evento (formato String, ex: "14:00").
 * @property localEvento Local onde o evento será realizado.
 * @property descricao Descrição detalhada do evento.
 * @property modalidades Lista de IDs das modalidades associadas ao evento.
 */
data class EventoCriarRequestDTO(
    val data: String,                // Data do evento (formato String)
    val hora: String,                // Hora do evento (formato String)
    val localEvento: String,         // Local onde o evento será realizado
    val descricao: String,           // Descrição do evento
    val modalidades: List<Int>,      // Lista de IDs das modalidades associadas ao evento
)