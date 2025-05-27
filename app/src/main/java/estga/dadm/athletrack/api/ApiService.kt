package estga.dadm.athletrack.api

import retrofit2.Call
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Response

// Define os modelos de dados e serviços para comunicação com a API.

// Modelo de dados para quando é necessário enviar apenas o id do sócio.
data class idRequest(
    val id: Int
)

// Modelo de dados do pedido de login.
data class UserRequest(
    val idSocio: Int,
    val password: String
)

// Modelo de dados da resposta do usuário.
data class User(
    val idSocio: Int,
    val nome: String,
    val tipo: String
)

// Modelo de dados para criação de usuário.
data class UserCreate(
    val password: String,
    val nome: String,
    val tipo: String,
    val modalidades: List<Int>
)

// Modelo de dados para exclusão de usuário.
data class UserDelete(
    val idSocio: Int,
)

// Interface para serviços relacionados ao usuário.
interface UserService {
    @POST("/api/user/login")
    fun login(@Body request: UserRequest): Call<User>

    @POST("/api/user/listar")
    suspend fun listar(): List<User>

    @POST("/api/user/criar")
    suspend fun criar(@Body request: UserCreate): ResponseBody

    @POST("/api/user/eliminar/{idParaEliminar}")
    suspend fun eliminar(
        @Path("idParaEliminar") idParaEliminar: Int,
        @Body request: UserRequest
    ): ResponseBody
}

// Modelo da Request do treino.
data class TreinosRequest(
    val idSocio: Int,
    val diaSemana: String
)

// Modelo de Resposta do treino.
data class Treino(
    val idTreino: Int,
    val nomeModalidade: String,
    val diaSemana: String,
    val hora: String,
    val qrCode: String
)

// Modelo de dados para criação de treino.
data class TreinoCreateRequest(
    val diaSemana: String,
    val hora: String,
    val qrCode: String,
    val idModalidade: Int,
    val idProfessor: Int
)

// Modelo de dados para exclusão de treino.
data class TreinoDeleteRequest(
    val qrCode: String,
    val idSocio: Int,
    val password: String
)

// Interface para serviços relacionados a treinos.
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
    suspend fun apagarTreino(@Body request: TreinoDeleteRequest): Response<ResponseBody>

    @POST("/api/treinos/criar")
    suspend fun criarTreino(@Body request: TreinoCreateRequest): Response<ResponseBody>
}

// Modelo da Request dos eventos.
data class EventosRequest(
    val idSocio: Int,
)

// Modelo de Resposta dos Eventos.
data class Evento(
    val id: Int,              // ID do evento
    val localEvento: String,
    val data: String,
    val hora: String,
    val descricao: String
)

// Modelo de dados para criação de evento.
data class EventoCriarRequest(
    val data: String,
    val hora: String,
    val localEvento: String,
    val descricao: String,
    val modalidades: List<Int>
)

data class EventoApagarRequest(
    val id: Int,
    val idProfessor: Int,
    val password: String
)

// Interface para serviços relacionados a eventos.
interface EventosService {
    @POST("/api/eventos/listar")
    suspend fun getEventos(@Body request: EventosRequest): List<Evento>

    @POST("/api/eventos/criar")
    suspend fun criarEvento(@Body request: EventoCriarRequest)

    @POST("/api/eventos/apagar")
    suspend fun apagarEvento(@Body request: EventoApagarRequest): Response<ResponseBody>
}

// Modelo da Request de presença.
data class PresencaRequest(
    val idSocio: Int,
    val qrCode: String,
    val estado: Boolean
)

// Modelo de Resposta de presença.
data class PresencaResponse(
    val sucesso: Boolean,
    val mensagem: String
)

// Modelo de lista de presenças.
data class PresencaListResponse(
    val id: Int,
    val nome: String,
    var estado: Boolean,
    var qrCode: Boolean
)

// Interface para serviços relacionados a presenças.
interface PresencasService {
    @POST("/api/presencas/registar")
    suspend fun registarPresenca(@Body request: PresencaRequest): PresencaResponse

    @POST("/api/presencas/registarmanual")
    suspend fun registarPresencasManuais(@Body requests: List<PresencaRequest>): Boolean

    @POST("/api/presencas/listar")
    suspend fun listarPresencas(@Body request: idRequest): List<PresencaListResponse>
}

// Modelo de dados para modalidades.
data class Modalidade(
    val id: Int,
    val nomeModalidade: String
)

// Interface para serviços relacionados a modalidades.
interface ModalidadesService {
    @POST("/api/modalidade/listar")
    suspend fun listarModalidades(): List<Modalidade>
}