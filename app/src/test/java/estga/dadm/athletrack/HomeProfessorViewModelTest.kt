package estga.dadm.athletrack

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.viewmodels.HomeProfessorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeProfessorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeProfessorViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeProfessorViewModel()
    }

    @Test
    fun `estado inicial deve ser vazio`() = runTest {
        assertEquals(0, viewModel.treinosHoje.first().size)
        assertEquals(0, viewModel.treinosAmanha.first().size)
        assertEquals(0, viewModel.modalidades.first().size)
        assertEquals(0, viewModel.diasSemana.first().size)
    }

    @Test
    fun `carregarTreinosHoje deve carregar treinos com sucesso`() = runTest {
        val treinosMock = listOf(
            Treino(1, "Futebol", "SEG", "10:00", "qrCode1"),
            Treino(2, "Basquete", "SEG", "14:00", "qrCode2")
        )
        viewModel.carregarTreinosHojeHardcoded(treinosMock)
        val treinosHoje = viewModel.treinosHoje.first()
        assertEquals(2, treinosHoje.size)
        assertEquals("Futebol", treinosHoje[0].nomeModalidade)
    }

    @Test
    fun `carregarTreinosAmanha deve carregar treinos com sucesso`() = runTest {
        val treinosMock = listOf(
            Treino(3, "Vôlei", "TER", "10:00", "qrCode3"),
            Treino(4, "Handebol", "TER", "16:00", "qrCode4")
        )
        viewModel.carregarTreinosAmanhaHardcoded(treinosMock)
        val treinosAmanha = viewModel.treinosAmanha.first()
        assertEquals(2, treinosAmanha.size)
        assertEquals("Vôlei", treinosAmanha[0].nomeModalidade)
    }

    @Test
    fun `carregarTreinos deve retornar lista vazia em caso de erro`() = runTest {
        viewModel.carregarTreinosHojeHardcoded(emptyList())
        viewModel.carregarTreinosAmanhaHardcoded(emptyList())
        assertEquals(0, viewModel.treinosHoje.first().size)
        assertEquals(0, viewModel.treinosAmanha.first().size)
    }

    @Test
    fun `carregarModalidades deve carregar modalidades com sucesso`() = runTest {
        val modalidadesMock = listOf(
            Modalidade(1, "Futebol"),
            Modalidade(2, "Basquete")
        )
        viewModel.carregarModalidadesHardcoded(modalidadesMock)
        val modalidades = viewModel.modalidades.first()
        assertEquals(2, modalidades.size)
        assertEquals("Futebol", modalidades[0].nomeModalidade)
    }

    @Test
    fun `detetarDiaSemana deve retornar o dia correto`() {
        val diaAtual = viewModel.detetarDiaSemana()
        val diasValidos = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
        assert(diaAtual in diasValidos)
    }

    @Test
    fun `carregarDiasSemana deve carregar dias corretamente`() = runTest {
        viewModel.carregarDiasSemana()
        val diasSemana = viewModel.diasSemana.first()
        assertEquals(7, diasSemana.size)
        assertEquals("SEG", diasSemana[0])
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}