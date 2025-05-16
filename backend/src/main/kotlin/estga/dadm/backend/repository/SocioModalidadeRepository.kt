package estga.dadm.backend.repository

import estga.dadm.backend.keys.SocioModalidadeId
import estga.dadm.backend.model.SocioModalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocioModalidadeRepository : JpaRepository<SocioModalidade, SocioModalidadeId> {
    fun findBySocioId(socioId: Int): List<SocioModalidade>

    fun findByModalidadeId(modalidadeId: Int): List<SocioModalidade>
}
