package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import estga.dadm.athletrack.api.Evento
import estga.dadm.athletrack.api.EventosRequest
import estga.dadm.athletrack.api.RetrofitClient

class CalendarViewModel : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos

    fun selecionarData(data: LocalDate) {
        _selectedDate.value = data
    }

    
    fun irParaMesAnterior() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    fun irParaMesSeguinte() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    fun carregarEventosParaMes(idSocio: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.eventosService.getEventos(EventosRequest(idSocio = idSocio))
                _eventos.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

