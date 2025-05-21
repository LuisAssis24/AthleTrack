package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.EventoCriarRequestDTO
import estga.dadm.athletrack.api.EventosRequest


class AdicionarEventoViewModel : ViewModel() {
    private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())
    val modalidades: StateFlow<List<Modalidade>> = _modalidades

    fun carregarModalidades() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.modalidadesService.listarModalidades()
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
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Verificar se o evento já existe
                val eventosExistentes = RetrofitClient.eventosService.getEventos(
                    EventosRequest(idSocio = 0) // Substitua por um ID válido, se necessário
                )
                val eventoDuplicado = eventosExistentes.any { evento ->
                    evento.localEvento == local &&
                            evento.data == data &&
                            evento.hora == hora &&
                            evento.descricao == descricao
                }

                if (eventoDuplicado) {
                    onError("Evento já existe com os mesmos dados.")
                    return@launch
                }

                // Criar o evento se não for duplicado
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
                onError("Erro ao criar evento: ${e.message}")
            }
        }
    }



}