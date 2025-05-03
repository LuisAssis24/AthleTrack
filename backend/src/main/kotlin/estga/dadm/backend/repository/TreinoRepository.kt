package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository
import estga.dadm.backend.model.DiaDaSemana

interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findByProfessorIdSocioAndDiaSemana(id_socio: Int, diaSemana: DiaDaSemana): List<Treino>
}