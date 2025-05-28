package estga.dadm.backend.repository

import estga.dadm.backend.model.Modalidade
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Modalidade.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação e consulta de modalidades.
 */
@Repository
interface ModalidadeRepository : JpaRepository<Modalidade, Int> {
    /**
     * Busca modalidades por uma lista de IDs.
     *
     * @param ids Lista de IDs das modalidades a serem buscadas.
     * @return Lista de entidades Modalidade correspondentes aos IDs fornecidos.
     */
    fun findByIdIn(ids: List<Int>): List<Modalidade>
}