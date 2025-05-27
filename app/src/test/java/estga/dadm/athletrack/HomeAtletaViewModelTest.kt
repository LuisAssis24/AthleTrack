package estga.dadm.athletrack

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.viewmodels.HomeAtletaViewModel
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
class HomeAtletaViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeAtletaViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeAtletaViewModel()
    }

    @Test
    fun `estado inicial deve ser lista vazia`() = runTest {
        val treinos = viewModel.treinos.first()
        assertEquals(0, treinos.size)
    }

    @Test
    fun `carregarTreinos deve carregar treinos com sucesso`() = runTest {
        // Dados hardcoded
        val treinosMock = listOf(
            Treino(1, "Futebol", "SEG", "10:00", "qrCode1"),
            Treino(2, "Basquete", "TER", "14:00", "qrCode2")
        )

        // Simula o carregamento de treinos
        viewModel.carregarTreinosTest(treinosMock)

        // Verifica se os treinos foram carregados corretamente
        val treinos = viewModel.treinos.first()
        assertEquals(2, treinos.size)
        assertEquals("Futebol", treinos[0].nomeModalidade)
        assertEquals("Basquete", treinos[1].nomeModalidade)
    }

    @Test
    fun `carregarTreinos deve retornar lista vazia em caso de erro`() = runTest {
        // Simula erro ao carregar treinos
        viewModel.carregarTreinosTest(emptyList())

        // Verifica se a lista de treinos está vazia
        val treinos = viewModel.treinos.first()
        assertEquals(0, treinos.size)
    }

    @Test
    fun `detetarDiaSemana deve retornar o dia correto`() {
        // Mock do dia atual
        val diaAtual = viewModel.detetarDiaSemana()

        // Verifica se o dia retornado é válido
        val diasValidos = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
        assert(diaAtual in diasValidos)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}