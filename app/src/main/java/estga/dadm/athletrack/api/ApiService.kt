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
data class User(
    val idSocio: Int,
    val nome: String,
    val tipo: String
)

interface LoginService {
    @Headers("Content-Type: application/json")
    @POST("/api/login")
    fun login(@Body request: LoginRequest): Call<User>
}

// Modelo da Request do treino
data class TreinosRequest(
    val idSocio: Int,
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

    @POST("/api/treinos/aluno")
    suspend fun getTreinosAluno(@Body request: TreinosRequest): List<Treino>
}

data class EventosRequest(
    val idSocio: Int,
)

// Modelo de Resposta dos Eventos
data class Evento(
    val localEvento: String,
    val data: String,
    val hora: String,
    val descricao: String
)

interface EventosService {
    @POST("/api/eventos/listar")
    suspend fun getEventos(@Body request: EventosRequest): List<Evento>
}

