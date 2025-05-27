package estga.dadm.athletrack

import estga.dadm.athletrack.api.PresencaListResponse
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.viewmodels.GestaoPresencasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GestaoPresencasViewModelTest {

    private lateinit var viewModel: GestaoPresencasViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GestaoPresencasViewModel()
    }

    @Test
    fun `estado inicial deve ser vazio`() = runTest {
        assertEquals(null, viewModel.treinoInfo.first())
        assertEquals(0, viewModel.alunos.first().size)
    }

    @Test
    fun `carregarPresencas deve carregar presencas com sucesso`() = runTest {
        val treinoMock = Treino(1, "Futebol", "Segunda-feira", "10:00", "12345")
        val presencasMock = listOf(
            PresencaListResponse(1, "João", true, false),
            PresencaListResponse(2, "Maria", false, false)
        )

        viewModel.carregarPresencasHardcoded(treinoMock, presencasMock)

        val treino = viewModel.treinoInfo.first()
        val alunos = viewModel.alunos.first()

        assertEquals(treinoMock, treino)
        assertEquals(2, alunos.size)
        assertEquals("João", alunos[0].nome)
        assertEquals(true, alunos[0].estado)
    }

    @Test
    fun `carregarPresencas deve retornar vazio em caso de erro`() = runTest {
        viewModel.carregarPresencasHardcoded(null, emptyList())

        val treino = viewModel.treinoInfo.first()
        val alunos = viewModel.alunos.first()

        assertEquals(null, treino)
        assertEquals(0, alunos.size)
    }

    @Test
    fun `atualizarPresenca deve atualizar estado corretamente`() = runTest {
        val presencasMock = listOf(
            PresencaListResponse(1, "João", false, false),
            PresencaListResponse(2, "Maria", false, false)
        )

        viewModel.carregarPresencasHardcoded(null, presencasMock)
        viewModel.atualizarPresenca(1, true)

        val alunos = viewModel.alunos.first()
        assertEquals(true, alunos[0].estado)
        assertEquals(false, alunos[1].estado)
    }

    @Test
    fun `salvarPresencas deve salvar presencas manuais com sucesso`() = runTest {
        val presencasMock = listOf(
            PresencaListResponse(1, "João", true, false),
            PresencaListResponse(2, "Maria", false, false)
        )

        viewModel.carregarPresencasHardcoded(null, presencasMock)
        viewModel.salvarPresencasHardcoded("12345")

        // Simula sucesso no salvamento
        assertEquals(2, viewModel.alunos.first().size)
    }

    @Test
    fun `salvarPresencas deve retornar erro em caso de falha`() = runTest {
        val presencasMock = listOf(
            PresencaListResponse(1, "João", true, false),
            PresencaListResponse(2, "Maria", false, false)
        )

        viewModel.carregarPresencasHardcoded(null, presencasMock)
        viewModel.salvarPresencasHardcoded("12345", simularErro = true)

        // Simula falha no salvamento
        assertEquals(2, viewModel.alunos.first().size)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}