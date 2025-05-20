package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserCreate
import estga.dadm.athletrack.api.UserRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class GestaoAtletaViewModel : ViewModel() {
    private val _atletas = MutableStateFlow<List<User>>(emptyList())
    val atletas: StateFlow<List<User>> = _atletas

    fun carregarAtletas() {
        viewModelScope.launch {
            try {
                val todos = RetrofitClient.loginService.listar()
                _atletas.value = todos.filter { it.tipo.lowercase() == "atleta" }
            } catch (e: Exception) {
                _atletas.value = emptyList()
            }
        }
    }

    fun criarAtleta(request: UserCreate, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val resposta = RetrofitClient.loginService.criar(request)
                carregarAtletas()

                // Buscar o último utilizador criado pelo nome
                val todos = RetrofitClient.loginService.listar()
                val novoUser = todos.lastOrNull { it.nome == request.nome && it.tipo == "atleta" }
                if (novoUser != null) {
                    callback(true, "Utilizador criado!!\nID = ${novoUser.idSocio}")
                } else {
                    callback(true, resposta.toString())
                }
            } catch (e: Exception) {
                callback(false, "Erro ao criar atleta: ${e.message}")
            }
        }
    }

    fun apagarAtletaComSenha(
        idAtleta: Int,
        idProfessor: Int,
        senha: String,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val login = UserRequest(idProfessor, senha)
                val resposta: ResponseBody = RetrofitClient.loginService.eliminar(idAtleta, login)
                carregarAtletas()
                callback(true, resposta.string())
            } catch (e: Exception) {
                callback(false, "Erro ao apagar atleta: ${e.message}")
            }
        }
    }
}
