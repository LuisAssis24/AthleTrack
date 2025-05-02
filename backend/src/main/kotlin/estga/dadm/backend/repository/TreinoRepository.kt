package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findByProfessorIdSocioAndData(
        idSocio: Int,
        diaSemana: DiaDaSemana
    ): List<Treino>
}