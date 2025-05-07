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

// Modelo da Request do treino
data class TreinosRequest(
    val idProfessor: Int,
    val diaSemana: String
)

// Modelo de Resposta do treino
data class Treino(
    val nomeModalidade: String,
    val diaSemana: String,
    val hora: String,
    val qrCode: String
)

interface TreinosService {
    @POST("/api/treinos/hoje")
    suspend fun getTreinosHoje(@Body request: TreinosRequest): List<Treino>

    @POST("/api/treinos/amanha")
    suspend fun getTreinosAmanha(@Body request: TreinosRequest): List<Treino>
}


interface EventosService {
    @POST("/api/eventos/data")
    suspend fun getEventosPorData(@Body data: String): List<Evento>
}

// Modelo de Evento
data class Evento(
    val idEvento: Long,
    val localEvento: String,
    val data: String,
    val hora: String
)