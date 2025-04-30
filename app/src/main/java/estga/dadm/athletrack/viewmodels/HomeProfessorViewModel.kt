package estga.dadm.athletrack.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Treino
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import estga.dadm.athletrack.api.ProfessorIdDTO

class HomeProfessorViewModel : ViewModel() {

    val treinosHoje = mutableStateOf<List<Treino>>(emptyList())
    val treinosAmanha = mutableStateOf<List<Treino>>(emptyList())

    fun carregarTreinos(idProfessor: Int) {
        viewModelScope.launch {
            try {
                val request = ProfessorIdDTO(idProfessor)
                val hoje = RetrofitClient.treinosService.getTreinosHoje(request)
                val amanha = RetrofitClient.treinosService.getTreinosAmanha(request)
                treinosHoje.value = hoje
                treinosAmanha.value = amanha
            } catch (e: Exception) {
                Log.e("ViewModel", "Erro ao carregar treinos: ${e.message}")
            }
        }
    }
}