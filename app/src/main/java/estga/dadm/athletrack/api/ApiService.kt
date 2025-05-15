package estga.dadm.athletrack.api

import retrofit2.Call
import retrofit2.http.*

// Modelo de dados para quando é necessário enviar apenas o id do sócio
data class idRequest(
    val idSocio: Int
)

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

data class TreinoCreateRequest(
    val diaSemana: String,
    val hora: String,
    val qrCode: String,
    val idModalidade: Int,
    val idProfessor: Int
)

data class TreinoDeleteRequest(
    val qrCode: String
)

interface TreinosService {
    @POST("/api/treinos/hoje")
    suspend fun getTreinosHoje(@Body request: TreinosRequest): List<Treino>

    @POST("/api/treinos/amanha")
    suspend fun getTreinosAmanha(@Body request: TreinosRequest): List<Treino>

    @POST("/api/treinos/aluno")
    suspend fun getTreinosAluno(@Body request: TreinosRequest): List<Treino>

    @POST("/api/treinos")
    suspend fun listarTodosOsTreinos(@Body request: idRequest): List<Treino>

    @POST("/api/treinos/apagar")
    suspend fun apagarTreino(@Body request: TreinoDeleteRequest): String

    @POST("/api/treinos/criar")
    suspend fun criarTreino(@Body request: TreinoCreateRequest): String
}

data class EventosRequest(
    val idSocio: Int,
)

data class Modalidade(
    val id: Int,
    val nomeModalidade: String
)

// Modelo de Resposta dos Eventos
data class Evento(
    val localEvento: String,
    val data: String,
    val hora: String,
    val descricao: String
)

data class EventoCriarRequestDTO(
    val data: String,
    val hora: String,
    val localEvento: String,
    val descricao: String,
    val modalidades: List<Int>
)

interface EventosService {
    @POST("/api/eventos/listar")
    suspend fun getEventos(@Body request: EventosRequest): List<Evento>

    @POST("/api/eventos/criar")
    suspend fun criarEvento(@Body request: EventoCriarRequestDTO)

    @POST("/api/modalidades")
    suspend fun listarModalidades(): List<Modalidade>
}

data class PresencaRequest(
    val idSocio: Int,
    val qrCode: String
)

data class PresencaResponse(
    val sucesso: Boolean,
    val mensagem: String
)

interface PresencasService {
    @POST("/api/presencas/registar")
    suspend fun registarPresenca(@Body request: PresencaRequest): PresencaResponse
}
