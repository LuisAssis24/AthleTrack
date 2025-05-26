package estga.dadm.backend.repository

import estga.dadm.backend.model.EventoModalidade
import estga.dadm.backend.keys.EventoModalidadeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade EventoModalidade.
 */
@Repository
interface EventoModalidadeRepository : JpaRepository<EventoModalidade, EventoModalidadeId> {
    /**
     * Busca associações de evento-modalidade por uma lista de IDs de modalidades.
     */
    fun findByModalidadeIdIn(modalidadesIds: List<Int>): List<EventoModalidade>
}