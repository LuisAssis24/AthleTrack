package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.*
import estga.dadm.athletrack.api.*
import estga.dadm.athletrack.api.RetrofitClient.eventosService
import org.jetbrains.annotations.TestOnly

/**
 * ViewModel responsável por gerenciar o estado e a lógica de negócios do calendário.
 */
class CalendarViewModel : ViewModel() {

    // Estado interno que armazena a data selecionada no calendário.
    private val _selectedDate = MutableStateFlow(LocalDate.now())

    /**
     * Estado público que expõe a data selecionada para observação.
     */
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // Estado interno que armazena o mês atual exibido no calendário.
    private val _currentMonth = MutableStateFlow(YearMonth.now())

    /**
     * Estado público que expõe o mês atual para observação.
     */
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    // Estado interno que armazena a lista de eventos carregados.
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())

    /**
     * Estado público que expõe a lista de eventos para observação.
     */
    val eventos: StateFlow<List<Evento>> = _eventos

    /**
     * Atualiza a data selecionada no calendário.
     *
     * @param data A nova data a ser selecionada.
     */
    fun selecionarData(data: LocalDate) {
        _selectedDate.value = data
    }

    /**
     * Move o calendário para o mês anterior.
     */
    fun irParaMesAnterior() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    /**
     * Move o calendário para o mês seguinte.
     */
    fun irParaMesSeguinte() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

    /**
     * Carrega os eventos do mês atual para o usuário especificado.
     *
     * @param idSocio O ID do sócio cujos eventos devem ser carregados.
     */
    fun carregarEventosParaMes(idSocio: Int) {
        viewModelScope.launch {
            try {
                // Faz a chamada ao serviço remoto para obter os eventos.
                val response = eventosService.getEventos(EventosRequest(idSocio = idSocio))
                _eventos.value = response
            } catch (e: Exception) {
                // Imprime o erro no log em caso de falha.
                e.printStackTrace()
            }
        }
    }

    fun apagarEvento(
        idEvento: Int,
        idProfessor: Int,
        password: String,
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = eventosService.apagarEvento(
                    EventoApagarRequest(idEvento, idProfessor, password)
                )

                if (response.isSuccessful) {
                    onResult(true, "Evento eliminado com sucesso.")
                } else {
                    onResult(false, "Erro ao eliminar evento: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onResult(false, "Exceção ao eliminar evento: ${e.message}")
            }
        }
    }



    /**
     * Carrega eventos hardcoded para testes
     *
     * @param eventos A lista de eventos a serem carregados.
     */
    @TestOnly
    fun carregarEventosTest(eventos: List<Evento>) {
        _eventos.value = eventos
    }

}

