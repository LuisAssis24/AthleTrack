package estga.dadm.backend.controller

import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.evento.*
import estga.dadm.backend.repository.*
import estga.dadm.backend.dto.evento.EventoCriarRequestDTO
import estga.dadm.backend.model.*
import estga.dadm.backend.repository.EventoRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime

/**
 * Controlador REST responsável pelas operações relacionadas a eventos.
 *
 * Fornece endpoints para listar eventos associados às modalidades de um sócio
 * e para criar eventos vinculados a modalidades específicas.
 */
@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val eventoModalidadeRepository: EventoModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository,
    private val modalidadeRepository: ModalidadeRepository,
    private val eventoRepository: EventoRepository
) {

    /**
     * Lista eventos associados às modalidades do sócio informado.
     *
     * @param request Objeto EventoRequestDTO contendo o ID do sócio.
     * @return Lista de EventoResponseDTO representando os eventos das modalidades do sócio.
     */
    @PostMapping("/listar")
    fun listarPorIdSocio(@RequestBody request: EventoRequestDTO): List<EventoResponseDTO> {
        val socioModalidades = socioModalidadeRepository.findBySocioId(request.idSocio)
        val modalidadesIds = socioModalidades.map { it.modalidade.id }

        val eventos = eventoModalidadeRepository.findByModalidadeIdIn(modalidadesIds)
            .map { it.evento }
            .map { evento ->
                EventoResponseDTO(
                    id = evento.id,
                    localEvento = evento.localEvento,
                    data = evento.data,
                    hora = evento.hora,
                    descricao = evento.descricao
                )
            }

        return eventos
    }

    /**
     * Cria um evento e associa às modalidades informadas.
     *
     * @param request Objeto EventoCriarRequestDTO contendo os dados do evento e IDs das modalidades.
     * @throws RuntimeException se ocorrer erro ao criar o evento ou associar modalidades.
     */
    @PostMapping("/criar")
    fun criarEvento(@RequestBody request: EventoCriarRequestDTO) {
        try {
            // Verifica se já existe um evento igual
            val eventoExistente = eventoRepository.findByLocalEventoAndDataAndHoraAndDescricao(
                request.localEvento,
                LocalDate.parse(request.data),
                LocalTime.parse(request.hora).withSecond(0),
                request.descricao
            )
            if (eventoExistente != null) {
                throw IllegalArgumentException("Evento já existe com os mesmos dados.")
            }

            // Cria o evento
            val evento = Evento(
                id = 0,
                localEvento = request.localEvento,
                data = LocalDate.parse(request.data),
                hora = LocalTime.parse(request.hora).withSecond(0),
                descricao = request.descricao
            )
            val eventoSalvo = eventoRepository.save(evento)

            // Associa as modalidades ao evento
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

    /**
     * Remove um evento e todas as suas associações com modalidades.
     *
     * Este endpoint recebe um ID de evento, remove todas as ligações do evento com modalidades
     * e, em seguida, exclui o próprio evento do repositório.
     *
     * @param request Objeto IdRequestDTO contendo o ID do evento a ser removido.
     * @throws RuntimeException se ocorrer algum erro durante a exclusão.
     */
    @PostMapping("/apagar")
    @Transactional
    fun apagarEvento(@RequestBody request: IdRequestDTO): ResponseEntity<String> {
        return try {
            val evento = eventoRepository.findById(request.id)
                .orElseThrow { IllegalArgumentException("Evento não encontrado.") }

            eventoModalidadeRepository.deleteByEventoId(evento.id)
            eventoRepository.delete(evento)
            ResponseEntity.ok("Evento apagado com sucesso.")
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(404).body(e.message)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body("Erro ao apagar evento: ${e.message}")
        }
    }
}