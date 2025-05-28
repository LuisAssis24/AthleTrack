package estga.dadm.backend.repository

import estga.dadm.backend.model.Evento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Evento.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação e consulta de eventos.
 */
@Repository
interface EventoRepository : JpaRepository<Evento, Int> {

    /**
     * Busca todos os eventos que ocorrem na data especificada.
     *
     * @param data Data do evento a ser buscado.
     * @return Lista de eventos que ocorrem na data informada.
     */
    fun findByData(data: LocalDate): List<Evento>

    /**
     * Busca um evento pelo local, data, hora e descrição.
     *
     * @param localEvento Local onde o evento ocorre.
     * @param data Data do evento.
     * @param hora Hora do evento.
     * @param descricao Descrição do evento.
     * @return Evento correspondente aos parâmetros informados, ou null se não encontrado.
     */
    fun findByLocalEventoAndDataAndHoraAndDescricao(
        localEvento: String,
        data: LocalDate,
        hora: LocalTime,
        descricao: String
    ): Evento?
}