package estga.dadm.athletrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.api.TreinoCreateRequest
import estga.dadm.athletrack.api.TreinoDeleteRequest
import estga.dadm.athletrack.api.TreinosRequest
import estga.dadm.athletrack.api.idRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate


class HomeProfessorViewModel : ViewModel() {
    private val api = RetrofitClient.treinosService

    private val _treinosHoje = MutableStateFlow<List<Treino>>(emptyList())
    val treinosHoje: StateFlow<List<Treino>> = _treinosHoje

    private val _treinosAmanha = MutableStateFlow<List<Treino>>(emptyList())
    val treinosAmanha: StateFlow<List<Treino>> = _treinosAmanha

    private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())
    val modalidades: StateFlow<List<Modalidade>> = _modalidades

    private val _diasSemana = MutableStateFlow<List<String>>(emptyList())
    val diasSemana: StateFlow<List<String>> = _diasSemana


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

    fun carregarModalidades() {
        viewModelScope.launch {
            try {
                _modalidades.value = RetrofitClient.modalidadesService.listarModalidades()
            } catch (_: Exception) {
            }
        }
    }

    suspend fun carregarModalidadesDoSocio(idSocio: Int) {
        try {
            val resultado = RetrofitClient.modalidadesService
                .listarPorId(idRequest(idSocio))
            Log.d("DEBUG_MODALIDADES", "Recebido: ${resultado.map { it.nomeModalidade }}")
            _modalidades.value = resultado
        } catch (e: Exception) {
            Log.e("DEBUG_MODALIDADES", "Erro: ${e.message}")
            _modalidades.value = emptyList()
        }
    }

    fun carregarDiasSemana() {
        _diasSemana.value = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
    }

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

    private val _treinosTodos = MutableStateFlow<List<Treino>>(emptyList())
    val treinosTodos: StateFlow<List<Treino>> = _treinosTodos

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

    fun apagarTreino(idSocio: Int, password: String, qrCode: String, onDone: (Boolean, String) -> Unit) {
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

}
