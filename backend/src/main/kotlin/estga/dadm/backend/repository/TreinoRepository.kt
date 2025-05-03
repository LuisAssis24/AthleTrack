package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository

import estga.dadm.backend.enum.DiaSemana

interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findByProfessorIdSocioAndDiaSemana(id_socio: Int, diaSemana: DiaSemana): List<Treino>
}