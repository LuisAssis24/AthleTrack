package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.Evento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Modalidade
import estga.dadm.athletrack.api.EventoCriarRequestDTO
import estga.dadm.athletrack.api.EventosRequest
import org.jetbrains.annotations.TestOnly

/**
 * ViewModel responsável por gerenciar a lógica de negócios para adicionar eventos e carregar modalidades.
 */
class AdicionarEventoViewModel : ViewModel() {

    // Estado interno que armazena a lista de modalidades disponíveis.
    private val _modalidades = MutableStateFlow<List<Modalidade>>(emptyList())

    /**
     * Estado público que expõe a lista de modalidades para observação.
     */
    val modalidades: StateFlow<List<Modalidade>> = _modalidades

    // Lista interna para armazenar eventos adicionados hardcoded.
    private val eventosAdicionados = mutableListOf<Evento>() // Lista interna para eventos adicionados

    /**
     * Carrega a lista de modalidades disponíveis a partir do serviço remoto.
     * Atualiza o estado interno `_modalidades` com os dados recebidos.
     */
    fun carregarModalidades() {
        viewModelScope.launch {
            try {
                // Faz a chamada ao serviço remoto para listar modalidades.
                val response = RetrofitClient.modalidadesService.listarModalidades()
                _modalidades.value = response
            } catch (e: Exception) {
                // Imprime o erro no log em caso de falha.
                e.printStackTrace()
            }
        }
    }

    /**
     * Adiciona um novo evento ao sistema.
     *
     * @param data A data do evento no formato "yyyy-MM-dd".
     * @param hora A hora do evento no formato "HH:mm".
     * @param local O local onde o evento será realizado.
     * @param descricao Uma descrição detalhada do evento.
     * @param modalidades Uma lista de IDs das modalidades associadas ao evento.
     * @param onSuccess Callback a ser executado em caso de sucesso.
     * @param onError Callback a ser executado em caso de erro, recebendo uma mensagem de erro.
     */
    fun adicionarEvento(
        idSocio: Int, // ID do sócio que está criando o evento.
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
                // Verifica se já existe um evento com os mesmos dados.
                val eventosExistentes = RetrofitClient.eventosService.getEventos(
                    EventosRequest(idSocio = idSocio) // Substitua por um ID válido, se necessário.
                )
                val eventoDuplicado = eventosExistentes.any { evento ->
                    evento.localEvento == local &&
                            evento.data == data &&
                            evento.hora == hora &&
                            evento.descricao == descricao
                }

                if (eventoDuplicado) {
                    // Retorna um erro se o evento já existir.
                    onError("Evento já existe com os mesmos dados.")
                    return@launch
                }

                // Cria o evento se não for duplicado.
                val request = EventoCriarRequestDTO(
                    data = data,
                    hora = hora,
                    localEvento = local,
                    descricao = descricao,
                    modalidades = modalidades,
                )
                RetrofitClient.eventosService.criarEvento(request)
                // Executa o callback de sucesso.
                onSuccess()
            } catch (e: Exception) {
                // Imprime o erro no log e executa o callback de erro.
                e.printStackTrace()
                onError("Erro ao criar evento: ${e.message}")
            }
        }
    }

    /**
     * Carrega modalidades hardcoded para testes ou desenvolvimento.
     * Substitui o estado interno `_modalidades` com a lista fornecida.
     *
     * @param modalidades Lista de modalidades a serem carregadas.
     */
    @TestOnly
    fun carregarModalidadesTest(modalidades: List<Modalidade>) {
        _modalidades.value = modalidades
    }

    /**
     * Adiciona um evento hardcoded para testes ou desenvolvimento.
     * Verifica se já existe um evento com a mesma descrição e retorna o resultado.
     *
     * @param data A data do evento.
     * @param hora A hora do evento.
     * @param local O local do evento.
     * @param descricao A descrição do evento.
     * @param modalidades Lista de IDs das modalidades associadas ao evento.
     * @param onResult Callback que recebe um booleano indicando se o evento foi adicionado com sucesso.
     */
    @TestOnly
    fun adicionarEventoTest(
        data: String,
        hora: String,
        local: String,
        descricao: String,
        modalidades: List<Int>,
        onResult: (Boolean) -> Unit,
        simularFalha: Boolean = false
    ) {
        if (simularFalha) {
            onResult(false)
            return
        }

        val eventoDuplicado = eventosAdicionados.any { evento ->
            evento.localEvento == local &&
                    evento.data == data &&
                    evento.hora == hora &&
                    evento.descricao == descricao
        }

        /*if (eventoDuplicado) {
            onResult(false)
        } else {
            val novoId = (eventosAdicionados.maxOfOrNull { it.id } ?: 0) + 1 // Gera um ID único
            eventosAdicionados.add(Evento(novoId, local, data, hora, descricao))
            onResult(true)
        }*/
    }

}