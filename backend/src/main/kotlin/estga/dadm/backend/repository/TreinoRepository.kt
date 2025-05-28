package estga.dadm.backend.repository

import estga.dadm.backend.model.Treino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD e consultas customizadas relacionadas à entidade Treino.
 *
 * Esta interface estende JpaRepository para fornecer métodos padrão de persistência,
 * além de métodos customizados para manipulação e consulta de treinos.
 */
@Repository
interface TreinoRepository : JpaRepository<Treino, Int> {
    /**
     * Busca todos os treinos de um professor específico em um determinado dia da semana,
     * ordenados por hora de início de forma ascendente.
     *
     * @param professorId ID do professor cujos treinos serão buscados.
     * @param diaSemana Dia da semana (ex: "Segunda-feira") para filtrar os treinos.
     * @return Lista de treinos do professor no dia da semana informado, ordenados por hora.
     */
    fun findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId: Int, diaSemana: String?): List<Treino>

    /**
     * Busca todos os treinos de determinadas modalidades em um dia da semana,
     * ordenados por hora de início de forma ascendente.
     *
     * @param modalidadesIds Lista de IDs das modalidades a serem buscadas.
     * @param diaSemana Dia da semana (ex: "Terça-feira") para filtrar os treinos.
     * @return Lista de treinos das modalidades no dia da semana informado, ordenados por hora.
     */
    fun findByModalidadeIdInAndDiaSemanaOrderByHoraAsc(modalidadesIds: List<Int>, diaSemana: String?): List<Treino>

    /**
     * Busca um treino pelo seu QR code.
     *
     * @param qrCode Código QR associado ao treino.
     * @return Treino correspondente ao QR code informado, ou null se não encontrado.
     */
    fun findByQrCode(qrCode: String): Treino?
}