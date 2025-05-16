package estga.dadm.backend.controller

import estga.dadm.backend.repository.*
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.dto.modalidade.ModalidadeDTO


@RestController
@RequestMapping("/api/modalidade")
class ModalidadeController(private val modalidadeRepository: ModalidadeRepository) {


    @PostMapping("/listarTodas")
    fun listarTodasModalidades(): List<ModalidadeDTO> {
        val modalidades = modalidadeRepository.findAll()
        return modalidades.map { modalidade ->
            ModalidadeDTO(
                id = modalidade.id,
                nomeModalidade = modalidade.nomeModalidade
            )
        }
    }
}