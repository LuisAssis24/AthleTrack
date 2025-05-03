package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository

interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findByProfessorIdSocioAndDiaSemana(professorId: Int, diaSemana: String?): List<Treino>

}