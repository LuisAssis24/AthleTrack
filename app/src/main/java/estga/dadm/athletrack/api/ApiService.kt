package estga.dadm.athletrack.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalTime

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


data class Aula(
    val nomeAula: String,
    val data: LocalDate,
    val hora: LocalTime,
)

interface AulasService {
    @GET("aulas/hoje")
    suspend fun getAulasDeHoje(@Query("professor") nome: String): List<Aula>

    @GET("aulas/amanha")
    suspend fun getAulasDeAmanha(@Query("professor") nome: String): List<Aula>
}