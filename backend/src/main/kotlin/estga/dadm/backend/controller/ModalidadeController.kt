package estga.dadm.backend.controller

import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.repository.*
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.dto.modalidade.ModalidadeDTO

/**
 * Controlador REST responsável pelas operações relacionadas às modalidades.
 *
 * Fornece endpoints para listar todas as modalidades disponíveis e para listar
 * as modalidades associadas a um sócio específico.
 */
@RestController
@RequestMapping("/api/modalidade")
class ModalidadeController(
    private val modalidadeRepository: ModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository
) {

    /**
     * Lista todas as modalidades disponíveis no sistema.
     *
     * @return Lista de objetos ModalidadeDTO representando todas as modalidades.
     */
    @PostMapping("/listar")
    fun listarTodasModalidades(): List<ModalidadeDTO> {
        val modalidades = modalidadeRepository.findAll()
        return modalidades.map { modalidade ->
            ModalidadeDTO(
                id = modalidade.id,
                nomeModalidade = modalidade.nomeModalidade
            )
        }
    }

    /**
     * Lista as modalidades associadas a um sócio específico.
     *
     * @param id Objeto IdRequestDTO contendo o ID do sócio.
     * @return Lista de ModalidadeDTO representando as modalidades do sócio.
     */
    @PostMapping("/listarporid")
    fun listarPorId(@RequestBody id: IdRequestDTO): List<ModalidadeDTO> {
        val socioModalidades = socioModalidadeRepository.findBySocioId(id.id)

        return socioModalidades.map { ligacao ->
            ModalidadeDTO(
                id = ligacao.modalidade.id,
                nomeModalidade = ligacao.modalidade.nomeModalidade
            )
        }
    }
}