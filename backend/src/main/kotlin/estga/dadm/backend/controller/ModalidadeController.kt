package estga.dadm.backend.controller

import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.repository.*
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.dto.modalidade.ModalidadeDTO


@RestController
@RequestMapping("/api/modalidade")
class ModalidadeController(private val modalidadeRepository: ModalidadeRepository,
                           private val socioModalidadeRepository: SocioModalidadeRepository) {


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