package estga.dadm.backend.controller

import estga.dadm.backend.dto.evento.*
import estga.dadm.backend.repository.*
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.dto.evento.EventoCriarRequestDTO
import estga.dadm.backend.model.Evento
import estga.dadm.backend.model.EventoModalidade
import estga.dadm.backend.repository.EventoRepository

import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime


@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val eventoModalidadeRepository: EventoModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository,
    private val modalidadeRepository: ModalidadeRepository,
    private val eventoRepository: EventoRepository

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
    fun criarEvento(@RequestBody request: EventoCriarRequestDTO) {
        try {
            // Verificar se já existe um evento igual
            val eventoExistente = eventoRepository.findByLocalEventoAndDataAndHoraAndDescricao(
                request.localEvento, LocalDate.parse(request.data), LocalTime.parse(request.hora), request.descricao
            )
            if (eventoExistente != null) {
                throw IllegalArgumentException("Evento já existe com os mesmos dados.")
            }

            // Criar o evento
            val evento = Evento(
                id = 0,
                localEvento = request.localEvento,
                data = LocalDate.parse(request.data),
                hora = LocalTime.parse(request.hora),
                descricao = request.descricao
            )
            val eventoSalvo = eventoRepository.save(evento)

            // Associar as modalidades ao evento
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
    }

    }