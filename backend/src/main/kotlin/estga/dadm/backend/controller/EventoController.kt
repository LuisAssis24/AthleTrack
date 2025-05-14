package estga.dadm.backend.controller

import estga.dadm.backend.dto.evento.EventoRequestDTO
import estga.dadm.backend.dto.evento.EventoResponseDTO
import estga.dadm.backend.repository.EventoModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDate


@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val eventoModalidadeRepository: EventoModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository
) {

    @PostMapping("/listar")
    fun listarPorIdSocio(@RequestBody request: EventoRequestDTO): List<EventoResponseDTO> {
        val socioModalidades = socioModalidadeRepository.findBySocioId(request.idSocio)

        val modalidadesIds = socioModalidades.map { it.modalidade.id }

        val eventos = eventoModalidadeRepository.findByModalidadeIdIn(modalidadesIds)
            .map { relacao ->
                val evento = relacao.evento
                EventoResponseDTO(
                    localEvento = evento.localEvento,
                    data = evento.data,
                    hora = evento.hora,
                    descricao = evento.descricao
                )
            }
            .filter { it.data.isEqual(LocalDate.now()) || it.data.isAfter(LocalDate.now()) }

        return eventos
    }

    @PostMapping("/criar")
    fun criarEvento(@RequestBody request: EventoRequestDTO) {
    }
}