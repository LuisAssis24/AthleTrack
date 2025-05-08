package estga.dadm.backend.repository

import estga.dadm.backend.model.Evento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface EventoRepository : JpaRepository<Evento, Long> {

    fun findByData(data: LocalDate): List<Evento>
}