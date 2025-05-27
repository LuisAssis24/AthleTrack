package estga.dadm.athletrack

import estga.dadm.athletrack.api.Evento
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.viewmodels.AdicionarEventoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdicionarEventoViewModelTest {

    private lateinit var viewModel: AdicionarEventoViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AdicionarEventoViewModel()
    }

    @Test
    fun `estado inicial deve ser vazio`() = runTest {
        assertEquals(0, viewModel.modalidades.first().size)
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
    fun `carregarModalidades deve retornar lista vazia em caso de erro`() = runTest {
        viewModel.carregarModalidadesHardcoded(emptyList())
        val modalidades = viewModel.modalidades.first()
        assertEquals(0, modalidades.size)
    }

    @Test
    fun `adicionarEvento deve adicionar evento com sucesso`() = runTest {
        viewModel.adicionarEventoHardcoded(
            data = "2023-12-25",
            hora = "10:00",
            local = "Ginásio",
            descricao = "Treino de Natal",
            modalidades = listOf(1),
            onResult = { sucesso ->
                assertEquals(true, sucesso)
            }
        )
    }

    @Test
    fun `adicionarEvento deve retornar erro para evento duplicado`() = runTest {
        viewModel.adicionarEventoHardcoded(
            data = "2023-12-25",
            hora = "10:00",
            local = "Ginásio",
            descricao = "Treino de Natal",
            modalidades = listOf(1),
            onResult = { sucesso ->
                assertEquals(true, sucesso)
            }
        )

        viewModel.adicionarEventoHardcoded(
            data = "2023-12-25",
            hora = "10:00",
            local = "Ginásio",
            descricao = "Treino de Natal",
            modalidades = listOf(1),
            onResult = { sucesso ->
                assertEquals(false, sucesso)
            }
        )
    }

    @Test
    fun `adicionarEvento deve retornar erro em caso de falha`() = runTest {
        viewModel.adicionarEventoHardcoded(
            data = "2023-12-25",
            hora = "10:00",
            local = "Ginásio",
            descricao = "Treino de Natal",
            modalidades = listOf(1),
            simularFalha = true,
            onResult = { sucesso ->
                assertEquals(false, sucesso) // Espera falha
            }
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}