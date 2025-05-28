package estga.dadm.backend.repository

import estga.dadm.backend.model.EventoModalidade
import estga.dadm.backend.keys.EventoModalidadeId
import estga.dadm.backend.model.Evento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade EventoModalidade.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação de associações entre eventos e modalidades.
 */
@Repository
interface EventoModalidadeRepository : JpaRepository<EventoModalidade, EventoModalidadeId> {
    /**
     * Busca todas as associações EventoModalidade cujos IDs de modalidade estejam na lista fornecida.
     *
     * @param modalidadesIds Lista de IDs das modalidades a serem buscadas.
     * @return Lista de entidades EventoModalidade correspondentes.
     */
    fun findByModalidadeIdIn(modalidadesIds: List<Int>): List<EventoModalidade>

    /**
     * Remove todas as associações EventoModalidade relacionadas ao evento com o ID fornecido.
     *
     * @param id ID do evento cujas associações devem ser removidas.
     */
    fun deleteByEventoId(id: Int)
}