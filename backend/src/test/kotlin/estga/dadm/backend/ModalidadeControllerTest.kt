package estga.dadm.backend

import estga.dadm.backend.controller.ModalidadeController
import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.modalidade.ModalidadeDTO
import estga.dadm.backend.model.Modalidade
import estga.dadm.backend.model.SocioModalidade
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ModalidadeControllerTest {

    private lateinit var modalidadeController: ModalidadeController
    private lateinit var modalidadeRepository: ModalidadeRepository
    private lateinit var socioModalidadeRepository: SocioModalidadeRepository

    @BeforeEach
    fun setUp() {
        // Mockando os repositórios
        modalidadeRepository = mock(ModalidadeRepository::class.java)
        socioModalidadeRepository = mock(SocioModalidadeRepository::class.java)

        // Inicializando o controller com os mocks
        modalidadeController = ModalidadeController(modalidadeRepository, socioModalidadeRepository)
    }

    @Test
    fun `listarTodasModalidades deve retornar todas as modalidades`() {
        // Dados hardcoded
        val modalidade1 = Modalidade(1, "Yoga")
        val modalidade2 = Modalidade(2, "Pilates")
        val modalidades = listOf(modalidade1, modalidade2)

        // Mockando o comportamento do repositório
        `when`(modalidadeRepository.findAll()).thenReturn(modalidades)

        // Executando o método
        val resultado = modalidadeController.listarTodasModalidades()

        // Verificando o resultado
        assertEquals(2, resultado.size)
        assertEquals("Yoga", resultado[0].nomeModalidade)
        assertEquals("Pilates", resultado[1].nomeModalidade)
    }

    @Test
    fun `listarPorId deve retornar modalidades associadas ao socio`() {
        // Dados hardcoded
        val user = User(1, "Usuário A", "atleta", "senha123")
        val modalidade1 = Modalidade(1, "Yoga")
        val modalidade2 = Modalidade(2, "Pilates")
        val socioModalidade1 = SocioModalidade(user, modalidade1)
        val socioModalidade2 = SocioModalidade(user, modalidade2)
        val socioModalidades = listOf(socioModalidade1, socioModalidade2)

        // Mockando o comportamento do repositório
        `when`(socioModalidadeRepository.findBySocioId(1)).thenReturn(socioModalidades)

        // Executando o método
        val resultado = modalidadeController.listarPorId(IdRequestDTO(1))

        // Verificando o resultado
        assertEquals(2, resultado.size)
        assertEquals("Yoga", resultado[0].nomeModalidade)
        assertEquals("Pilates", resultado[1].nomeModalidade)
    }

    @Test
    fun `listarPorId deve retornar lista vazia se socio nao tiver modalidades`() {
        // Mockando o comportamento do repositório para retornar lista vazia
        `when`(socioModalidadeRepository.findBySocioId(1)).thenReturn(emptyList())

        // Executando o método
        val resultado = modalidadeController.listarPorId(IdRequestDTO(1))

        // Verificando o resultado
        assertTrue(resultado.isEmpty())
    }

    @Test
    fun `listarTodasModalidades deve retornar lista vazia se nao houver modalidades`() {
        // Mockando o comportamento do repositório para retornar lista vazia
        `when`(modalidadeRepository.findAll()).thenReturn(emptyList())

        // Executando o método
        val resultado = modalidadeController.listarTodasModalidades()

        // Verificando o resultado
        assertTrue(resultado.isEmpty())
    }
}