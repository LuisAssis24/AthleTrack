package estga.dadm.athletrack

import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserRequest
import estga.dadm.athletrack.other.UserPreferences
import estga.dadm.athletrack.viewmodels.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var userPreferences: UserPreferences

    @Before
    fun setUp() {
        userPreferences = mock(UserPreferences::class.java)
        viewModel = LoginViewModel(userPreferences)
    }

    @Test
    fun `login deve ser bem-sucedido com credenciais válidas`() = runTest {
        val userMock = User(1, "João", "atleta")
        val socio = 1
        val password = "senha123"

        viewModel.login(
            socio = socio,
            password = password,
            onSuccess = { user ->
                assertEquals(userMock.idSocio, user.idSocio)
                assertEquals(userMock.nome, user.nome)
                assertEquals(userMock.tipo, user.tipo)
            },
            onError = { fail("Erro inesperado no login") }
        )
    }

    @Test
    fun `login deve falhar com credenciais inválidas`() = runTest {
        val socio = 1
        val password = "senhaErrada"

        viewModel.login(
            socio = socio,
            password = password,
            onSuccess = { fail("Login não deveria ser bem-sucedido") },
            onError = { mensagem ->
                assertEquals("Credenciais inválidas", mensagem)
            }
        )
    }

    @Test
    fun `login deve retornar erro em caso de falha de conexão`() = runTest {
        val socio = 1
        val password = "senha123"

        viewModel.login(
            socio = socio,
            password = password,
            onSuccess = { fail("Login não deveria ser bem-sucedido") },
            onError = { mensagem ->
                assertEquals("Erro: Falha na conexão", mensagem)
            }
        )
    }

    @Test
    fun `login deve retornar mensagem de erro personalizada`() = runTest {
        val socio = 1
        val password = "senha123"

        viewModel.login(
            socio = socio,
            password = password,
            onSuccess = { fail("Login não deveria ser bem-sucedido") },
            onError = { mensagem ->
                assertEquals("Utilizador inválido", mensagem)
            }
        )
    }
}