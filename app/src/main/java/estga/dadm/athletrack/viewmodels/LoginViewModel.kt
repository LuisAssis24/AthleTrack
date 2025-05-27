package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.*
import estga.dadm.athletrack.api.*
import estga.dadm.athletrack.other.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.*

/**
 * ViewModel responsável pelo processo de login do usuário.
 *
 * @property userPreferences Instância de `UserPreferences` para gerenciar o estado de login do usuário.
 */
class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    /**
     * Realiza o login do usuário.
     *
     * @param socio O ID do sócio (usuário) que está tentando fazer login.
     * @param password A senha do sócio.
     * @param onSuccess Callback executado em caso de sucesso, recebendo o objeto `User`.
     * @param onError Callback executado em caso de erro, recebendo uma mensagem de erro.
     */
    fun login(
        socio: Int,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = UserRequest(socio, password)

        // Faz a chamada ao serviço de login usando Retrofit.
        RetrofitClient.loginService.login(request).enqueue(object : Callback<User> {
            /**
             * Callback chamado quando a resposta do servidor é recebida.
             *
             * @param call A chamada Retrofit que foi feita.
             * @param response A resposta recebida do servidor.
             */
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        // Salva o estado de login e executa o callback de sucesso.
                        viewModelScope.launch {
                            userPreferences.saveLoginState(user.idSocio, user.tipo, user.nome)
                            onSuccess(user)
                        }
                    } ?: onError("Utilizador inválido")
                } else {
                    onError("Credenciais inválidas")
                }
            }

            /**
             * Callback chamado quando ocorre uma falha na chamada ao servidor.
             *
             * @param call A chamada Retrofit que foi feita.
             * @param t O erro que ocorreu.
             */
            override fun onFailure(call: Call<User>, t: Throwable) {
                onError("Erro: ${t.message}")
            }
        })
    }
}
