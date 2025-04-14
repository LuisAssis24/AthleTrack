package estga.dadm.athletrack.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Modelo de dados do pedido
data class LoginRequest(
    val idSocio: Int,
    val password: String
)

// Modelo de dados da resposta
data class LoginResponse(
    val idSocio: Int,
    val nome: String,
    val tipo: String
)

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
