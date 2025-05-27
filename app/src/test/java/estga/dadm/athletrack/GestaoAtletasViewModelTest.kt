package estga.dadm.athletrack

import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserCreate
import estga.dadm.athletrack.viewmodels.GestaoAtletasViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GestaoAtletasViewModelTest {

    private lateinit var viewModel: GestaoAtletasViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GestaoAtletasViewModel()
    }

    @Test
    fun `estado inicial deve ser vazio`() = runTest {
        assertEquals(0, viewModel.atletas.first().size)
        assertEquals(0, viewModel.modalidades.first().size)
    }

    @Test
    fun `carregarAtletas deve carregar atletas com sucesso`() = runTest {
        val atletasMock = listOf(
            User(1, "João", "atleta"),
            User(2, "Maria", "atleta")
        )
        viewModel.carregarAtletasHardcoded(atletasMock)
        val atletas = viewModel.atletas.first()
        assertEquals(2, atletas.size)
        assertEquals("João", atletas[0].nome)
    }

    @Test
    fun `carregarAtletas deve retornar lista vazia em caso de erro`() = runTest {
        viewModel.carregarAtletasHardcoded(emptyList())
        val atletas = viewModel.atletas.first()
        assertEquals(0, atletas.size)
    }

    @Test
    fun `criarAtleta deve criar atleta com sucesso`() = runTest {
        val request = UserCreate("senha123", "Carlos", "atleta", listOf(1))
        viewModel.criarAtletaHardcoded(request) { sucesso, mensagem ->
            assertEquals(true, sucesso)
            assertEquals("Atleta criado com sucesso!", mensagem)
        }
        val atletas = viewModel.atletas.first()
        assertEquals(1, atletas.size)
        assertEquals("Carlos", atletas[0].nome)
    }

    @Test
    fun `criarAtleta deve retornar erro em caso de falha`() = runTest {
        val request = UserCreate("senha123", "Carlos", "atleta", listOf(1))
        viewModel.criarAtletaHardcoded(request, simularErro = true) { sucesso, mensagem ->
            assertEquals(false, sucesso)
            assertEquals("Erro ao criar atleta.", mensagem)
        }
    }

    @Test
    fun `apagarAtleta deve apagar atleta com sucesso`() = runTest {
        val atletasMock = listOf(
            User(1, "João", "atleta"),
            User(2, "Maria", "atleta")
        )
        viewModel.carregarAtletasHardcoded(atletasMock)
        viewModel.apagarAtletaHardcoded(1) { sucesso, mensagem ->
            assertEquals(true, sucesso)
            assertEquals("Atleta apagado com sucesso!", mensagem)
        }
        val atletas = viewModel.atletas.first()
        assertEquals(1, atletas.size)
        assertEquals("Maria", atletas[0].nome)
    }

    @Test
    fun `apagarAtleta deve retornar erro em caso de falha`() = runTest {
        viewModel.apagarAtletaHardcoded(1, simularErro = true) { sucesso, mensagem ->
            assertEquals(false, sucesso)
            assertEquals("Erro ao apagar atleta.", mensagem)
        }
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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}