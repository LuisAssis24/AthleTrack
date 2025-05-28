package estga.dadm.backend.repository

import estga.dadm.backend.keys.PresencaId
import estga.dadm.backend.model.Presenca
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Presenca.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação e consulta de presenças.
 */
@Repository
interface PresencaRepository : JpaRepository<Presenca, PresencaId> {

    /**
     * Busca todas as presenças de um sócio específico.
     *
     * @param socioId ID do sócio cujas presenças serão buscadas.
     * @return Lista de presenças associadas ao sócio informado.
     */
    fun findBySocioId(socioId: Int): List<Presenca>

    /**
     * Busca uma presença específica pelo ID do sócio e pelo ID do treino.
     *
     * @param socioId ID do sócio.
     * @param treinoId ID do treino.
     * @return Presença correspondente aos IDs informados, ou null se não encontrada.
     */
    fun findBySocioIdAndTreinoId(socioId: Int, treinoId: Int): Presenca?

    /**
     * Busca todas as presenças de um treino específico.
     *
     * @param treinoId ID do treino cujas presenças serão buscadas.
     * @return Lista de presenças associadas ao treino informado.
     */
    fun findByTreinoId(treinoId: Int): List<Presenca>
}