package estga.dadm.backend.repository

import estga.dadm.backend.keys.PresencaId
import estga.dadm.backend.model.Presenca
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Presenca.
 */
@Repository
interface PresencaRepository : JpaRepository<Presenca, PresencaId> {
    fun findBySocioId(socioId: Int): List<Presenca>

    /**
     * Busca uma presença por ID do sócio e ID do treino.
     */
    fun findBySocioIdAndTreinoId(socioId: Int, treinoId: Int): Presenca?

    /**
     * Busca todas as presenças de um treino.
     */
    fun findByTreinoId(treinoId: Int): List<Presenca>
}