package estga.dadm.athletrack.viewmodels

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.Aula
import estga.dadm.athletrack.api.RetrofitClient
import kotlinx.coroutines.launch

class HomeProfessorViewModel : ViewModel() {

    val aulasHoje = mutableStateOf<List<Aula>>(emptyList())
    val aulasAmanha = mutableStateOf<List<Aula>>(emptyList())

    fun carregarAulas(professor: String) {
        viewModelScope.launch {
            try {
                val aulasHojeResponse = RetrofitClient.aulasService.getAulasDeHoje(professor)
                val aulasAmanhaResponse = RetrofitClient.aulasService.getAulasDeAmanha(professor)

                aulasHoje.value = aulasHojeResponse
                aulasAmanha.value = aulasAmanhaResponse

            } catch (e: Exception) {
                Log.e("HomeProfessorViewModel", "Erro ao carregar aulas: ${e.message}", e)
            }
        }
    }
}