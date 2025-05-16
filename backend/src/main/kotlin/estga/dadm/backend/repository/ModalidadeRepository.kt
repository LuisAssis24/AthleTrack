package estga.dadm.backend.repository

import estga.dadm.backend.model.Modalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ModalidadeRepository : JpaRepository<Modalidade, Int>{

    fun findByIdIn(ids: List<Int>): List<Modalidade>

}
