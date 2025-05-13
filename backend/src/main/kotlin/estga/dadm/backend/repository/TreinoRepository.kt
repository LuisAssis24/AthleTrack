package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TreinoRepository : JpaRepository<Treino, Long> {
    fun findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId: Int, diaSemana: String?): List<Treino>

    fun findByModalidadeIdInAndDiaSemanaOrderByHoraAsc(modalidadesIds: List<Int>, diaSemana: String?): List<Treino>

    fun findByQrCode(qrCode: String): Treino?

}
