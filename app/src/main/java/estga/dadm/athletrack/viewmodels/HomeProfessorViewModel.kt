package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                val resposta = api.criarTreino(
                    TreinoCreateRequest(diaSemana, hora, "", idModalidade, idProfessor)
                )
                callback(true, resposta)
            } catch (e: Exception) {
                callback(false, "Erro ao criar treino")
            }
        }
    }

    fun apagarTreino(qrCode: String, onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                api.apagarTreino(
                    TreinoDeleteRequest(qrCode)
                )
            } catch (_: Exception) { }
            onDone()
        }
    }

}
