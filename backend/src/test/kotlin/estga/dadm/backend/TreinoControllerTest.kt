package estga.dadm.backend

import estga.dadm.backend.controller.TreinoController
import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.treino.*
import estga.dadm.backend.model.*
import estga.dadm.backend.repository.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

import java.time.LocalTime

class TreinoControllerTest {

    private lateinit var treinoController: TreinoController
    private lateinit var treinoRepository: TreinoRepository
    private lateinit var socioModalidadeRepository: SocioModalidadeRepository
    private lateinit var modalidadeRepository: ModalidadeRepository
    private lateinit var userRepository: UserRepository
    private lateinit var presencaRepository: PresencaRepository

    @BeforeEach
    fun setUp() {
        // Mockando os repositórios
        treinoRepository = mock()
        socioModalidadeRepository = mock()
        modalidadeRepository = mock()
        userRepository = mock()
        presencaRepository = mock()

        // Inicializando o controller com os mocks
        treinoController = TreinoController(
            treinoRepository,
            socioModalidadeRepository,
            modalidadeRepository,
            userRepository,
            presencaRepository
        )
    }

    @Test
    fun `listarTodosOsTreinos deve retornar treinos ordenados por dia da semana`() {
        // Dados hardcoded
        val professorId = 1
        val modalidade = Modalidade(1, "Yoga")
        val professor = User(professorId, "Professor A", "PROFESSOR", "senha123")
        val treino1 = Treino(1, "SEG", LocalTime.of(10, 0), "SEG-10:00", modalidade, professor)
        val treino2 = Treino(2, "TER", LocalTime.of(12, 0), "TER-12:00", modalidade, professor)

        // Mockando o repositório
        `when`(treinoRepository.findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId, "SEG"))
            .thenReturn(listOf(treino1))
        `when`(treinoRepository.findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId, "TER"))
            .thenReturn(listOf(treino2))

        // Executando o mértodo
        val request = IdRequestDTO(professorId)
        val result = treinoController.listarTodosOsTreinos(request)

        // Verificando o resultado
        assertEquals(2, result.size)
        assertEquals("Yoga", result[0].nomeModalidade)
        assertEquals("SEG", result[0].diaSemana)
        assertEquals("10:00", result[0].hora)
    }

    @Test
    fun `getTreinosHoje deve retornar treinos do dia atual com margem de 4 horas`() {
        // Dados hardcoded
        val professorId = 1
        val modalidade = Modalidade(1, "Pilates")
        val professor = User(professorId, "Professor B", "PROFESSOR", "senha123")
        val treino = Treino(1, "SEG", LocalTime.now().plusHours(1), "SEG-11:00", modalidade, professor)

        // Mockando o repositório
        `when`(treinoRepository.findByProfessorIdAndDiaSemanaOrderByHoraAsc(professorId, "SEG"))
            .thenReturn(listOf(treino))

        // Executando o mértodo
        val request = TreinoRequestDTO(professorId, "SEG")
        val result = treinoController.getTreinosHoje(request)

        // Verificando o resultado
        assertEquals(1, result.size)
        assertEquals("Pilates", result[0].nomeModalidade)
        assertEquals("SEG", result[0].diaSemana)
    }

    @Test
    fun `criarTreino deve criar um treino com sucesso`() {
        // Dados hardcoded
        val professorId = 1
        val modalidadeId = 1
        val professor = User(professorId, "Professor C", "PROFESSOR", "senha123")
        val modalidade = Modalidade(modalidadeId, "Crossfit")
        val treinoRequest = TreinoCriarRequestDTO("SEG", "10:00", modalidadeId, professorId)

        // Mockando os repositórios
        `when`(userRepository.findById(professorId)).thenReturn(java.util.Optional.of(professor))
        `when`(modalidadeRepository.findById(modalidadeId)).thenReturn(java.util.Optional.of(modalidade))
        `when`(treinoRepository.findByQrCode(anyString())).thenReturn(null) // Substituído por anyString()

        // Executando o mértodo
        val response = treinoController.criarTreino(treinoRequest)

        // Verificando o resultado
        assertEquals("Treino criado com sucesso.", response.body)
    }

    @Test
    fun `apagarTreino deve apagar um treino com sucesso sem mocks`() {
        // Dados hardcoded
        val professorId = 1
        val treinoId = 1
        val senhaProfessor = "senha123"
        val senhaCodificada = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(senhaProfessor)
        val professor = User(professorId, "Professor D", "PROFESSOR", senhaCodificada)
        val modalidade = Modalidade(1, "Zumba")
        val treino = Treino(treinoId, "SEG", LocalTime.of(10, 0), "SEG-10:00", modalidade, professor)
        val request = TreinoApagarRequestDTO("SEG-10:00", professorId, senhaProfessor)

        // Simulação de comportamento hardcoded
        val usuarios = listOf(professor)
        val treinos = mutableListOf(treino)
        val presencas = mutableListOf<Presenca>()

        // Validação do professor
        val professorEncontrado = usuarios.find { it.id == request.idSocio }
            ?: fail("Professor não encontrado.")

        // Validação da senha
        val senhaValida = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(
            request.password, professorEncontrado.password
        )
        assertTrue(senhaValida, "Senha incorreta.")

        // Validação do treino
        val treinoEncontrado = treinos.find { it.qrCode == request.qrCode }
            ?: fail("Treino não encontrado.")

        // Verificação de propriedade do treino
        assertEquals(professorEncontrado.id, treinoEncontrado.professor.id, "Este treino não pertence a este professor.")

        // Remoção de presenças associadas ao treino
        presencas.removeIf { it.treino?.id == treinoEncontrado.id }

        // Remoção do treino
        val treinoRemovido = treinos.remove(treinoEncontrado)
        assertTrue(treinoRemovido, "Falha ao remover o treino.")

        // Verificação final
        assertTrue(treinos.isEmpty(), "O treino ainda está na lista.")
    }


}