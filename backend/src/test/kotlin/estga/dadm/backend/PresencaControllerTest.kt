package estga.dadm.backend

import estga.dadm.backend.controller.PresencaController
import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.treino.PresencaRequestDTO
import estga.dadm.backend.dto.treino.PresencaResponseDTO
import estga.dadm.backend.dto.treino.PresencaListResponseDTO
import estga.dadm.backend.model.*
import estga.dadm.backend.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import java.time.LocalTime

class PresencaControllerTest {

    private lateinit var presencaController: PresencaController
    private lateinit var userRepository: UserRepository
    private lateinit var treinoRepository: TreinoRepository
    private lateinit var presencaRepository: PresencaRepository
    private lateinit var socioModalidadeRepository: SocioModalidadeRepository

    @BeforeEach
    fun setUp() {
        // Mockando os repositórios
        userRepository = mock(UserRepository::class.java)
        treinoRepository = mock(TreinoRepository::class.java)
        presencaRepository = mock(PresencaRepository::class.java)
        socioModalidadeRepository = mock(SocioModalidadeRepository::class.java)

        // Inicializando o controller com os mocks
        presencaController = PresencaController(
            userRepository,
            treinoRepository,
            presencaRepository,
            socioModalidadeRepository
        )
    }

    @Test
    fun `registarPresencaQr deve registar presença com sucesso`() {
        // Dados hardcoded
        val treino = Treino(1, "Segunda-feira", LocalTime.of(10, 0), "qr123", Modalidade(1, "Yoga"), User(2, "Professor", "professor", "senha"))
        val socio = User(1, "Aluno A", "atleta", "senha")
        val request = PresencaRequestDTO(1, "qr123", true)
        val socioModalidade = SocioModalidade(socio, treino.modalidade)

        // Mockando os repositórios
        `when`(treinoRepository.findByQrCode("qr123")).thenReturn(treino)
        `when`(userRepository.findById(1)).thenReturn(java.util.Optional.of(socio))
        `when`(socioModalidadeRepository.findBySocioId(1)).thenReturn(listOf(socioModalidade))
        `when`(presencaRepository.existsById(any())).thenReturn(false)

        // Executando o mértodo
        val response = presencaController.registarPresencaQr(request)

        // Verificando o resultado
        assertEquals(200, response.statusCodeValue)
        assertEquals("Presença registada com sucesso.", response.body?.mensagem)
        assertTrue(response.body?.sucesso == true)
    }

    @Test
    fun `registarPresencaQr deve retornar erro para QR Code inválido`() {
        // Dados hardcoded
        val request = PresencaRequestDTO(1, "qrInvalido", true)

        // Mockando os repositórios
        `when`(treinoRepository.findByQrCode("qrInvalido")).thenReturn(null)

        // Executando o mértodo
        val response = presencaController.registarPresencaQr(request)

        // Verificando o resultado
        assertEquals(400, response.statusCodeValue)
        assertEquals("QR Code inválido.", response.body?.mensagem)
        assertFalse(response.body?.sucesso == true)
    }

    @Test
    fun `registarPresencaQr deve retornar erro para sócio não encontrado`() {
        // Dados hardcoded
        val treino = Treino(1, "Segunda-feira", LocalTime.of(10, 0), "qr123", Modalidade(1, "Yoga"), User(2, "Professor", "professor", "senha"))
        val request = PresencaRequestDTO(1, "qr123",true)

        // Mockando os repositórios
        `when`(treinoRepository.findByQrCode("qr123")).thenReturn(treino)
        `when`(userRepository.findById(1)).thenReturn(java.util.Optional.empty())

        // Executando o mértodo
        val response = presencaController.registarPresencaQr(request)

        // Verificando o resultado
        assertEquals(400, response.statusCodeValue)
        assertEquals("Sócio não encontrado.", response.body?.mensagem)
        assertFalse(response.body?.sucesso == true)
    }

    @Test
    fun `listarPresencas deve retornar lista de presenças`() {
        // Dados hardcoded
        val treino = Treino(1, "Segunda-feira", LocalTime.of(10, 0), "qr123", Modalidade(1, "Yoga"), User(2, "Professor", "professor", "senha"))
        val socio1 = User(1, "Aluno A", "atleta", "senha")
        val socio2 = User(2, "Aluno B", "atleta", "senha")
        val socioModalidade1 = SocioModalidade(socio1, treino.modalidade)
        val socioModalidade2 = SocioModalidade(socio2, treino.modalidade)
        val presenca = Presenca(socio1, treino, true, true)

        // Mockando os repositórios
        `when`(treinoRepository.findById(1)).thenReturn(java.util.Optional.of(treino))
        `when`(socioModalidadeRepository.findByModalidadeId(1)).thenReturn(listOf(socioModalidade1, socioModalidade2))
        `when`(presencaRepository.findBySocioIdAndTreinoId(1, 1)).thenReturn(presenca)

        // Executando o mértodo
        val response = presencaController.listarPresencas(IdRequestDTO(1))

        // Verificando o resultado
        assertEquals(2, response.size)
        assertEquals("Aluno A", response[0].nome)
        assertTrue(response[0].estado)
        assertEquals("Aluno B", response[1].nome)
        assertFalse(response[1].estado)
    }

    @Test
    fun `listarPresencas deve retornar lista vazia para treino inexistente`() {
        // Mockando os repositórios
        `when`(treinoRepository.findById(1)).thenReturn(java.util.Optional.empty())

        // Executando o mértodo
        val response = presencaController.listarPresencas(IdRequestDTO(1))

        // Verificando o resultado
        assertTrue(response.isEmpty())
    }
}