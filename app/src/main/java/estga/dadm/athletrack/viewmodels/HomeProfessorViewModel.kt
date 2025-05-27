package estga.dadm.athletrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.time.LocalDate

/**
 * ViewModel responsável por gerenciar os treinos e modalidades do professor.
 */
class HomeProfessorViewModel : ViewModel() {

    // Serviço para operações relacionadas a treinos.
    private val api = RetrofitClient.treinosService

    // Estado interno que armazena os treinos de hoje.
    private val _treinosHoje = MutableStateFlow<List<Treino>>(emptyList())

    /**
     * Estado público que expõe os treinos de hoje para observação.
     */
    val treinosHoje: StateFlow<List<Treino>> = _treinosHoje

    // Estado interno que armazena os treinos de amanhã.
    private val _treinosAmanha = MutableStateFlow<List<Treino>>(emptyList())

    /**
     * Estado público que expõe os treinos de amanhã para observação.
     */
    val treinosAmanha: StateFlow<List<Treino>> = _treinosAmanha

    // Estado interno que armazena a lista de modalidades.
    private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())

    /**
     * Estado público que expõe a lista de modalidades para observação.
     */
    val modalidades: StateFlow<List<Modalidade>> = _modalidades

    // Estado interno que armazena os dias da semana.
    private val _diasSemana = MutableStateFlow<List<String>>(emptyList())

    /**
     * Estado público que expõe os dias da semana para observação.
     */
    val diasSemana: StateFlow<List<String>> = _diasSemana

    /**
     * Carrega os treinos do professor para hoje e amanhã com base no dia da semana.
     *
     * @param idProfessor O ID do professor cujos treinos devem ser carregados.
     * @param diaSemana O dia da semana para o qual os treinos devem ser carregados.
     */
    fun carregarTreinos(idProfessor: Int, diaSemana: String) {
        viewModelScope.launch {
            try {
                val respostaHoje = api.getTreinosHoje(
                    TreinosRequest(idSocio = idProfessor, diaSemana = diaSemana)
                )
                _treinosHoje.value = respostaHoje

                val respostaAmanha = api.getTreinosAmanha(
                    TreinosRequest(idSocio = idProfessor, diaSemana = diaSemana)
                )
                _treinosAmanha.value = respostaAmanha
            } catch (e: Exception) {
                _treinosHoje.value = emptyList()
                _treinosAmanha.value = emptyList()
            }
        }
    }

    /**
     * Carrega a lista de modalidades disponíveis.
     */
    fun carregarModalidades() {
        viewModelScope.launch {
            try {
                _modalidades.value = RetrofitClient.modalidadesService.listarModalidades()
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Carrega os dias da semana.
     */
    fun carregarDiasSemana() {
        _diasSemana.value = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
    }

    /**
     * Detecta o dia da semana atual.
     *
     * @return Uma string representando o dia da semana atual (e.g., "SEG", "TER").
     */
    fun detetarDiaSemana(): String {
        val diaAtual = LocalDate.now().dayOfWeek.value
        return when (diaAtual) {
            1 -> "SEG"
            2 -> "TER"
            3 -> "QUA"
            4 -> "QUI"
            5 -> "SEX"
            6 -> "SAB"
            7 -> "DOM"
            else -> ""
        }
    }

    // Estado interno que armazena todos os treinos.
    private val _treinosTodos = MutableStateFlow<List<Treino>>(emptyList())

    /**
     * Estado público que expõe todos os treinos para observação.
     */
    val treinosTodos: StateFlow<List<Treino>> = _treinosTodos

    /**
     * Carrega todos os treinos do professor.
     *
     * @param idSocio O ID do sócio (professor) cujos treinos devem ser carregados.
     */
    fun carregarTodosOsTreinos(idSocio: Int) {
        viewModelScope.launch {
            try {
                val resposta = api.listarTodosOsTreinos(idRequest(idSocio))
                _treinosTodos.value = resposta
                Log.d("DEBUG", "Carregando treinos para ID: $idSocio")
                Log.d("DEBUG", "Recebido: ${resposta.map { it.nomeModalidade }}")
            } catch (e: Exception) {
                _treinosTodos.value = emptyList()
            }
        }
    }

    /**
     * Cria um novo treino.
     *
     * @param diaSemana O dia da semana do treino.
     * @param hora A hora do treino.
     * @param idModalidade O ID da modalidade associada ao treino.
     * @param idProfessor O ID do professor responsável pelo treino.
     * @param callback Callback a ser executado após a criação, indicando sucesso ou erro.
     */
    fun criarTreino(
        diaSemana: String,
        hora: String,
        idModalidade: Int,
        idProfessor: Int,
        callback: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.criarTreino(
                    TreinoCreateRequest(diaSemana, hora, "", idModalidade, idProfessor)
                )

                if (response.isSuccessful) {
                    val mensagem = response.body()?.string() ?: "Treino criado com sucesso"
                    callback(true, mensagem)
                    carregarTodosOsTreinos(idProfessor) // força atualização após criar
                } else {
                    val erro = response.errorBody()?.string() ?: "Erro ao criar treino"
                    callback(false, erro)
                }
            } catch (e: Exception) {
                callback(false, "Erro ao criar treino")
            }
        }
    }

    /**
     * Apaga um treino.
     *
     * @param idSocio O ID do sócio (professor) que está apagando o treino.
     * @param password A senha do professor para autenticação.
     * @param qrCode O QR Code do treino a ser apagado.
     * @param onDone Callback a ser executado após a exclusão, indicando sucesso ou erro.
     */
    fun apagarTreino(
        idSocio: Int,
        password: String,
        qrCode: String,
        onDone: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = api.apagarTreino(
                    TreinoDeleteRequest(qrCode = qrCode, idSocio = idSocio, password = password)
                )
                if (response.isSuccessful) {
                    val mensagem = response.body()?.string() ?: "Sucesso"
                    onDone(true, mensagem)
                } else {
                    val erro = response.errorBody()?.string() ?: "Erro ao apagar treino"
                    onDone(false, erro)
                }
            } catch (e: Exception) {
                onDone(false, "Erro ao apagar treino")
            }
        }
    }

    /**
     * Carrega treinos hardcoded para testes ou desenvolvimento.
     *
     * @param treinos Lista de treinos a serem carregados.
     */
    @TestOnly
    fun carregarTreinosHojeTest(treinos: List<Treino>) {
        _treinosHoje.value = treinos
    }

    /**
     * Carrega treinos de amanhã hardcoded para testes ou desenvolvimento.
     *
     * @param treinos Lista de treinos a serem carregados.
     */
    @TestOnly
    fun carregarTreinosAmanhaTest(treinos: List<Treino>) {
        _treinosAmanha.value = treinos
    }

    /**
     * Carrega modalidades hardcoded para testes ou desenvolvimento.
     *
     * @param modalidades Lista de modalidades a serem carregadas.
     */
    @TestOnly
    fun carregarModalidadesTest(modalidades: List<Modalidade>) {
        _modalidades.value = modalidades
    }

}