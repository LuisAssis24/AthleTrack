package estga.dadm.backend.repository

import estga.dadm.backend.keys.SocioModalidadeId
import estga.dadm.backend.model.SocioModalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade SocioModalidade.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação e consulta de associações entre sócios e modalidades.
 */
@Repository
interface SocioModalidadeRepository : JpaRepository<SocioModalidade, SocioModalidadeId> {
    /**
     * Busca todas as associações de modalidades de um sócio específico.
     *
     * @param socioId ID do sócio cujas associações de modalidades serão buscadas.
     * @return Lista de entidades SocioModalidade associadas ao sócio informado.
     */
    fun findBySocioId(socioId: Int): List<SocioModalidade>

    /**
     * Busca todas as associações de sócios de uma modalidade específica.
     *
     * @param modalidadeId ID da modalidade cujas associações de sócios serão buscadas.
     * @return Lista de entidades SocioModalidade associadas à modalidade informada.
     */
    fun findByModalidadeId(modalidadeId: Int): List<SocioModalidade>
}