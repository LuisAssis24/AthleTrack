package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeAlunoViewModel: ViewModel() {
    private val api = RetrofitClient.treinosService

    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())
    val treinos: StateFlow<List<Treino>> = _treinos

    fun carregarTreinos(idSocio: Int, diaSemana: String){

        viewModelScope.launch {
            try {
                val resposta = api.getTreinosAluno(
                    TreinosRequest(idSocio = idSocio, diaSemana = diaSemana)
                )
                _treinos.value = resposta
            } catch (e: Exception) {
                _treinos.value = emptyList()
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
}