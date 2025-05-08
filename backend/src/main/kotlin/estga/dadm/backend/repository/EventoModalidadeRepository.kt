package estga.dadm.backend.repository

import estga.dadm.backend.model.EventoModalidade
import estga.dadm.backend.keys.EventoModalidadeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventoModalidadeRepository : JpaRepository<EventoModalidade, EventoModalidadeId> {
    fun findByModalidadeIdIn(modalidadesIds: List<Int>): List<EventoModalidade>
}
