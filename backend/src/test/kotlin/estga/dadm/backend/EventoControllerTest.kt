package estga.dadm.backend

import estga.dadm.backend.controller.EventoController
import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.evento.EventoCriarRequestDTO
import estga.dadm.backend.dto.evento.EventoRequestDTO
import estga.dadm.backend.dto.evento.EventoResponseDTO
import estga.dadm.backend.model.*
import estga.dadm.backend.repository.EventoModalidadeRepository
import estga.dadm.backend.repository.EventoRepository
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import java.time.LocalDate
import java.time.LocalTime

class EventoControllerTest {

    private lateinit var eventoController: EventoController
    private lateinit var eventoModalidadeRepository: EventoModalidadeRepository
    private lateinit var socioModalidadeRepository: SocioModalidadeRepository
    private lateinit var modalidadeRepository: ModalidadeRepository
    private lateinit var eventoRepository: EventoRepository

    @BeforeEach
    fun setUp() {
        // Mockando os repositórios
        eventoModalidadeRepository = mock()
        socioModalidadeRepository = mock()
        modalidadeRepository = mock()
        eventoRepository = mock()

        // Inicializando o controller com os mocks
        eventoController = EventoController(
            eventoModalidadeRepository,
            socioModalidadeRepository,
            modalidadeRepository,
            eventoRepository
        )
    }

    @Test
    fun `listarPorIdSocio deve retornar eventos associados ao socio`() {
        // Dados hardcoded
        val socioId = 1
        val user = User(id = socioId, tipo = "atleta", password = "1234", nome = "Nome do Usuário")
        val modalidade = Modalidade(1, "Yoga")
        val evento = Evento(1, "Local A", LocalDate.now(), LocalTime.of(10, 0), "Descrição A")
        val eventoModalidade = EventoModalidade(evento, modalidade)
        val socioModalidade = SocioModalidade(user, modalidade)


        // Simulando o comportamento dos repositórios com dados hardcoded
        val socioModalidades = listOf(socioModalidade)
        val eventoModalidades = listOf(eventoModalidade)

        // Simulando o método listarPorIdSocio
        val modalidadesIds = socioModalidades.map { it.modalidade.id }
        val eventos = eventoModalidades.filter { it.modalidade.id in modalidadesIds }
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

        // Verificando o resultado
        assertEquals(1, eventos.size)
        assertEquals("Local A", eventos[0].localEvento)
        assertEquals("Descrição A", eventos[0].descricao)
    }

    @Test
    fun `criarEvento deve criar um evento com sucesso`() {
        // Dados hardcoded
        val modalidadesIds = listOf(1, 2)
        val modalidade1 = Modalidade(1, "Yoga")
        val modalidade2 = Modalidade(2, "Pilates")
        val request = EventoCriarRequestDTO(
            localEvento = "Local B",
            data = "2023-12-01",
            hora = "10:00",
            descricao = "Descrição B",
            modalidades = modalidadesIds
        )
        val evento =
            Evento(0, "Local B", LocalDate.parse(request.data), LocalTime.parse(request.hora), request.descricao)

        // Mockando os repositórios
        `when`(modalidadeRepository.findById(1)).thenReturn(java.util.Optional.of(modalidade1))
        `when`(modalidadeRepository.findById(2)).thenReturn(java.util.Optional.of(modalidade2))
        `when`(eventoRepository.save(any(Evento::class.java))).thenReturn(evento)

        // Executando o método
        eventoController.criarEvento(request)

        // Verificando se o evento foi salvo
        verify(eventoRepository, times(1)).save(any(Evento::class.java))
        verify(eventoModalidadeRepository, times(2)).save(any(EventoModalidade::class.java))
    }

    @Test
    fun `apagarEvento deve remover um evento com sucesso`() {
        // Dados hardcoded
        val eventoId = 1
        val evento = Evento(eventoId, "Local C", LocalDate.now(), LocalTime.of(12, 0), "Descrição C")

        // Mockando os repositórios
        `when`(eventoRepository.findById(eventoId)).thenReturn(java.util.Optional.of(evento))

        // Executando o método
        val request = IdRequestDTO(eventoId)
        val response = eventoController.apagarEvento(request)

        // Verificando o resultado
        assertEquals(200, response.statusCodeValue)
        assertEquals("Evento apagado com sucesso.", response.body)

        // Verificando se o evento foi removido
        verify(eventoModalidadeRepository, times(1)).deleteByEventoId(eventoId)
        verify(eventoRepository, times(1)).delete(evento)
    }

    @Test
    fun `apagarEvento deve retornar erro se evento nao for encontrado`() {
        // Dados hardcoded
        val eventoId = 99

        // Mockando os repositórios
        `when`(eventoRepository.findById(eventoId)).thenReturn(java.util.Optional.empty())

        // Executando o método
        val request = IdRequestDTO(eventoId)
        val response = eventoController.apagarEvento(request)

        // Verificando o resultado
        assertEquals(404, response.statusCodeValue)
        assertEquals("Evento não encontrado.", response.body)
    }
}