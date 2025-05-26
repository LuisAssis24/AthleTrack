package estga.dadm.backend.repository

import estga.dadm.backend.model.Evento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Evento.
 */
@Repository
interface EventoRepository : JpaRepository<Evento, Int> {

    /**
     * Busca eventos por data.
     */
    fun findByData(data: LocalDate): List<Evento>

    /**
     * Busca um evento por local, data, hora e descrição.
     */
    fun findByLocalEventoAndDataAndHoraAndDescricao(
        localEvento: String,
        data: LocalDate,
        hora: LocalTime,
        descricao: String
    ): Evento?

}