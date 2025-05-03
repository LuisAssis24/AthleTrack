package estga.dadm.athletrack.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.api.TreinosRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeProfessorViewModel : ViewModel() {
    private val _treinosHoje = MutableStateFlow<List<Treino>>(emptyList())
    val treinosHoje: StateFlow<List<Treino>> = _treinosHoje

    private val _treinosAmanha = MutableStateFlow<List<Treino>>(emptyList())
    val treinosAmanha: StateFlow<List<Treino>> = _treinosAmanha

    fun carregarTreinos(idProfessor: Int, diaSemana: String) {

        viewModelScope.launch {
            try {
                val respostaHoje = RetrofitClient.treinosService.getTreinosHoje(
                    TreinosRequest(idProfessor = idProfessor, diaSemana = diaSemana)
                )
                _treinosHoje.value = respostaHoje

                val respostaAmanha = RetrofitClient.treinosService.getTreinosAmanha(
                    TreinosRequest(idProfessor = idProfessor, diaSemana = diaSemana)
                )
                _treinosAmanha.value = respostaAmanha

            } catch (e: Exception) {
                _treinosHoje.value = emptyList()
                _treinosAmanha.value = emptyList()
            }
        }
    }
}
