package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Treino.
 */
@Repository
interface TreinoRepository : JpaRepository<Treino, Int> {
    /**
     * Busca treinos de um professor em um determinado dia da semana, ordenados por hora.
     */
    fun findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId: Int, diaSemana: String?): List<Treino>

    /**
     * Busca treinos de determinadas modalidades em um dia da semana, ordenados por hora.
     */
    fun findByModalidadeIdInAndDiaSemanaOrderByHoraAsc(modalidadesIds: List<Int>, diaSemana: String?): List<Treino>

    /**
     * Busca um treino pelo seu QR code.
     */
    fun findByQrCode(qrCode: String): Treino?
}