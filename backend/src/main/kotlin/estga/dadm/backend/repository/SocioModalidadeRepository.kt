package estga.dadm.backend.repository

import estga.dadm.backend.keys.SocioModalidadeId
import estga.dadm.backend.model.SocioModalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade SocioModalidade.
 */
@Repository
interface SocioModalidadeRepository : JpaRepository<SocioModalidade, SocioModalidadeId> {
    /**
     * Busca todas as associações de modalidades de um sócio.
     */
    fun findBySocioId(socioId: Int): List<SocioModalidade>

    /**
     * Busca todas as associações de sócios de uma modalidade.
     */
    fun findByModalidadeId(modalidadeId: Int): List<SocioModalidade>
}