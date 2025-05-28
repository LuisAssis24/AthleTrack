package estga.dadm.athletrack.api

import retrofit2.Call
import retrofit2.http.*
import okhttp3.ResponseBody
import retrofit2.Response

// Define os modelos de dados e serviços para comunicação com a API.

/**
 * Modelo de dados para quando é necessário enviar apenas o id do sócio.
 * @property id Identificador único do sócio.
 */
data class IdRequest(
    val id: Int
)

/**
 * Modelo de dados do pedido de login.
 * @property idSocio Identificador único do sócio.
 * @property password Senha do sócio.
 */
data class UserRequest(
    val idSocio: Int,
    val password: String
)

/**
 * Modelo de dados da resposta do usuário.
 * @property idSocio Identificador único do sócio.
 * @property nome Nome do sócio.
 * @property tipo Tipo de sócio (ex.: aluno, professor).
 */
data class User(
    val idSocio: Int,
    val nome: String,
    val tipo: String
)

/**
 * Modelo de dados para criação de usuário.
 * @property password Senha do usuário.
 * @property nome Nome do usuário.
 * @property tipo Tipo de usuário.
 * @property modalidades Lista de identificadores das modalidades associadas ao usuário.
 */
data class UserCreate(
    val password: String,
    val nome: String,
    val tipo: String,
    val modalidades: List<Int>
)

/**
 * Interface para serviços relacionados ao usuário.
 */
interface UserService {
    /**
     * Realiza o login do usuário.
     * @param request Dados do login.
     * @return Retorna os dados do usuário autenticado.
     */
    @POST("/api/user/login")
    fun login(@Body request: UserRequest): Call<User>

    /**
     * Lista todos os usuários.
     * @return Retorna uma lista de usuários.
     */
    @POST("/api/user/listar")
    suspend fun listar(): List<User>

    /**
     * Cria um novo usuário.
     * @param request Dados para criação do usuário.
     * @return Retorna o corpo da resposta.
     */
    @POST("/api/user/criar")
    suspend fun criar(@Body request: UserCreate): ResponseBody

    /**
     * Exclui um usuário.
     * @param idParaEliminar Identificador do usuário a ser excluído.
     * @param request Dados do usuário que está realizando a exclusão.
     * @return Retorna o corpo da resposta.
     */
    @POST("/api/user/eliminar/{idParaEliminar}")
    suspend fun eliminar(
        @Path("idParaEliminar") idParaEliminar: Int,
        @Body request: UserRequest
    ): ResponseBody
}

/**
 * Modelo da Request do treino.
 * @property idSocio Identificador único do sócio.
 * @property diaSemana Dia da semana do treino.
 */
data class TreinosRequest(
    val idSocio: Int,
    val diaSemana: String
)

/**
 * Modelo de Resposta do treino.
 * @property idTreino Identificador único do treino.
 * @property nomeModalidade Nome da modalidade do treino.
 * @property diaSemana Dia da semana do treino.
 * @property hora Hora do treino.
 * @property qrCode Código QR associado ao treino.
 */
data class Treino(
    val idTreino: Int,
    val nomeModalidade: String,
    val diaSemana: String,
    val hora: String,
    val qrCode: String
)

/**
 * Modelo de dados para criação de treino.
 * @property diaSemana Dia da semana do treino.
 * @property hora Hora do treino.
 * @property qrCode Código QR associado ao treino.
 * @property idModalidade Identificador da modalidade.
 * @property idProfessor Identificador do professor responsável.
 */
data class TreinoCreateRequest(
    val diaSemana: String,
    val hora: String,
    val qrCode: String,
    val idModalidade: Int,
    val idProfessor: Int
)

/**
 * Modelo de dados para exclusão de treino.
 * @property qrCode Código QR do treino a ser excluído.
 * @property idSocio Identificador único do sócio.
 * @property password Senha do sócio.
 */
data class TreinoDeleteRequest(
    val qrCode: String,
    val idSocio: Int,
    val password: String
)

/**
 * Interface para serviços relacionados a treinos.
 */
interface TreinosService {
    /**
     * Obtém os treinos de hoje.
     * @param request Dados do sócio e dia da semana.
     * @return Retorna uma lista de treinos.
     */
    @POST("/api/treinos/hoje")
    suspend fun getTreinosHoje(@Body request: TreinosRequest): List<Treino>

    /**
     * Obtém os treinos de amanhã.
     * @param request Dados do sócio e dia da semana.
     * @return Retorna uma lista de treinos.
     */
    @POST("/api/treinos/amanha")
    suspend fun getTreinosAmanha(@Body request: TreinosRequest): List<Treino>

    /**
     * Obtém os treinos de um aluno específico.
     * @param request Dados do sócio e dia da semana.
     * @return Retorna uma lista de treinos.
     */
    @POST("/api/treinos/aluno")
    suspend fun getTreinosAluno(@Body request: TreinosRequest): List<Treino>

    /**
     * Lista todos os treinos.
     * @param request Dados do sócio.
     * @return Retorna uma lista de treinos.
     */
    @POST("/api/treinos")
    suspend fun listarTodosOsTreinos(@Body request: IdRequest): List<Treino>

    /**
     * Apaga um treino.
     * @param request Dados do treino a ser apagado.
     * @return Retorna a resposta da operação.
     */
    @POST("/api/treinos/apagar")
    suspend fun apagarTreino(@Body request: TreinoDeleteRequest): Response<ResponseBody>

    /**
     * Cria um novo treino.
     * @param request Dados para criação do treino.
     * @return Retorna a resposta da operação.
     */
    @POST("/api/treinos/criar")
    suspend fun criarTreino(@Body request: TreinoCreateRequest): Response<ResponseBody>
}

/**
 * Modelo da Request dos eventos.
 * @property idSocio Identificador único do sócio.
 */
data class EventosRequest(
    val idSocio: Int,
)

/**
 * Modelo de Resposta dos Eventos.
 * @property id Identificador único do evento.
 * @property localEvento Local onde o evento será realizado.
 * @property data Data do evento.
 * @property hora Hora do evento.
 * @property descricao Descrição do evento.
 */
data class Evento(
    val id: Int,
    val localEvento: String,
    val data: String,
    val hora: String,
    val descricao: String
)

/**
 * Modelo de dados para criação de evento.
 * @property data Data do evento.
 * @property hora Hora do evento.
 * @property localEvento Local onde o evento será realizado.
 * @property descricao Descrição do evento.
 * @property modalidades Lista de identificadores das modalidades associadas ao evento.
 */
data class EventoCriarRequest(
    val data: String,
    val hora: String,
    val localEvento: String,
    val descricao: String,
    val modalidades: List<Int>
)

/**
 * Modelo de dados para exclusão de evento.
 * @property id Identificador único do evento.
 * @property idProfessor Identificador do professor responsável.
 * @property password Senha do professor.
 */
data class EventoApagarRequest(
    val id: Int,
    val idProfessor: Int,
    val password: String
)

/**
 * Interface para serviços relacionados a eventos.
 */
interface EventosService {
    /**
     * Obtém a lista de eventos.
     * @param request Dados do sócio.
     * @return Retorna uma lista de eventos.
     */
    @POST("/api/eventos/listar")
    suspend fun getEventos(@Body request: EventosRequest): List<Evento>

    /**
     * Cria um novo evento.
     * @param request Dados para criação do evento.
     */
    @POST("/api/eventos/criar")
    suspend fun criarEvento(@Body request: EventoCriarRequest)

    /**
     * Apaga um evento.
     * @param request Dados do evento a ser apagado.
     * @return Retorna a resposta da operação.
     */
    @POST("/api/eventos/apagar")
    suspend fun apagarEvento(@Body request: EventoApagarRequest): Response<ResponseBody>
}

/**
 * Modelo da Request de presença.
 * @property idSocio Identificador único do sócio.
 * @property qrCode Código QR associado à presença.
 * @property estado Estado da presença (ex.: presente ou ausente).
 */
data class PresencaRequest(
    val idSocio: Int,
    val qrCode: String,
    val estado: Boolean
)

/**
 * Modelo de Resposta de presença.
 * @property sucesso Indica se a operação foi bem-sucedida.
 * @property mensagem Mensagem de retorno da operação.
 */
data class PresencaResponse(
    val sucesso: Boolean,
    val mensagem: String
)

/**
 * Modelo de lista de presenças.
 * @property id Identificador único da presença.
 * @property nome Nome do sócio.
 * @property estado Estado da presença (ex.: presente ou ausente).
 * @property qrCode Indica se há um código QR associado.
 */
data class PresencaListResponse(
    val id: Int,
    val nome: String,
    var estado: Boolean,
    var qrCode: Boolean
)

/**
 * Interface para serviços relacionados a presenças.
 */
interface PresencasService {
    /**
     * Registra a presença de um sócio.
     * @param request Dados da presença.
     * @return Retorna a resposta da operação.
     */
    @POST("/api/presencas/registar")
    suspend fun registarPresenca(@Body request: PresencaRequest): PresencaResponse

    /**
     * Registra presenças manualmente.
     * @param requests Lista de dados de presença.
     * @return Retorna um booleano indicando o sucesso da operação.
     */
    @POST("/api/presencas/registarmanual")
    suspend fun registarPresencasManuais(@Body requests: List<PresencaRequest>): Boolean

    /**
     * Lista as presenças de um sócio.
     * @param request Dados do sócio.
     * @return Retorna uma lista de presenças.
     */
    @POST("/api/presencas/listar")
    suspend fun listarPresencas(@Body request: IdRequest): List<PresencaListResponse>
}

/**
 * Modelo de dados para modalidades.
 * @property id Identificador único da modalidade.
 * @property nomeModalidade Nome da modalidade.
 */
data class Modalidade(
    val id: Int,
    val nomeModalidade: String
)

/**
 * Interface para serviços relacionados a modalidades.
 */
interface ModalidadesService {
    /**
     * Lista todas as modalidades disponíveis.
     * @return Retorna uma lista de modalidades.
     */
    @POST("/api/modalidade/listar")
    suspend fun listarModalidades(): List<Modalidade>
}