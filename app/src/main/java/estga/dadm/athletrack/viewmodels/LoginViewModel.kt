package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.User
import estga.dadm.athletrack.api.UserRequest
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.other.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    fun login(
        socio: Int,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        val request = UserRequest(socio, password)

        RetrofitClient.loginService.login(request).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        viewModelScope.launch {
                            userPreferences.saveLoginState(user.idSocio, user.tipo, user.nome)
                            onSuccess(user)
                        }
                    } ?: onError("Utilizador inválido")
                } else {
                    onError("Credenciais inválidas")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onError("Erro: ${t.message}")
            }
        })
    }
}
