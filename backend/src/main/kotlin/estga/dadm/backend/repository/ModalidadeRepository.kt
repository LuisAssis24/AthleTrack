package estga.dadm.backend.repository

import estga.dadm.backend.model.Modalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Modalidade.
 */
@Repository
interface ModalidadeRepository : JpaRepository<Modalidade, Int> {
    /**
     * Busca modalidades por uma lista de IDs.
     */
    fun findByIdIn(ids: List<Int>): List<Modalidade>
}