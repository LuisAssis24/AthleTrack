package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.EventoCriarRequestDTO


class AdicionarEventoViewModel : ViewModel() {
    private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())
    val modalidades: StateFlow<List<Modalidade>> = _modalidades

    fun carregarModalidades() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.eventosService.listarModalidades()
                _modalidades.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun adicionarEvento(
        data: String,
        hora: String,
        local: String,
        descricao: String,
        modalidades: List<Int>,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val request = EventoCriarRequestDTO(
                    data = data,
                    hora = hora,
                    localEvento = local,
                    descricao = descricao,
                    modalidades = modalidades,
                )
                RetrofitClient.eventosService.criarEvento(request)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}