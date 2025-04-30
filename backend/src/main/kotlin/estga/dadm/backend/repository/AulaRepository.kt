package estga.dadm.backend.repository

import estga.dadm.backend.model.Aula
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AulaRepository : JpaRepository<Aula, Long> {

    fun findByNomeProfessorAndData(professor: String, data: LocalDate): List<Aula>
}