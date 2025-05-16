package estga.dadm.backend.controller

import estga.dadm.backend.dto.evento.EventoRequestDTO
import estga.dadm.backend.dto.evento.EventoResponseDTO
import estga.dadm.backend.repository.EventoModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.dto.evento.EventoCriarRequestDTO
import estga.dadm.backend.model.Modalidade
import estga.dadm.backend.model.Evento
import estga.dadm.backend.model.EventoModalidade

import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime


@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val eventoModalidadeRepository: EventoModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository,
    private val modalidadeRepository: ModalidadeRepository

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

    @PostMapping("/modalidades")
    fun listarModalidades(): List<Modalidade> {
        //return modalidadeRepository.findAll()
        // Simulação de dados para achar o problema
        return listOf(
            Modalidade(id = 1, nomeModalidade = "Futebol"),
            Modalidade(id = 2, nomeModalidade = "Basquete"),
            Modalidade(id = 3, nomeModalidade = "Vôlei")
        )
    }


    @PostMapping("/criar")
    fun criarEvento(@RequestBody request: EventoCriarRequestDTO) {
        /*
        try {
            val evento = Evento(
                id = 0,
                localEvento = request.localEvento,
                data = LocalDate.parse(request.data),
                hora = LocalTime.parse(request.hora),
                descricao = request.descricao
            )



            val eventoSalvo = eventoModalidadeRepository.saveEvento(evento)

            request.modalidades.forEach { modalidadeId ->
                val modalidade = modalidadeRepository.findById(modalidadeId)
                    .orElseThrow { IllegalArgumentException("Modalidade $modalidadeId não encontrada.") }

                val eventoModalidade = EventoModalidade(
                    evento = eventoSalvo,
                    modalidade = modalidade
                )
                eventoModalidadeRepository.save(eventoModalidade)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Erro ao criar evento: ${e.message}")
        }
        */

        }

    }