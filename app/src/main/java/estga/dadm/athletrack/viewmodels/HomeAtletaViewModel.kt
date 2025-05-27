package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.*
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import java.time.LocalDate

/**
 * ViewModel responsável por gerenciar os treinos do atleta.
 */
class HomeAtletaViewModel: ViewModel() {
    // Serviço para operações relacionadas a treinos.
    private val apiTreinos = RetrofitClient.treinosService

    // Serviço para operações relacionadas a presenças.
    internal val apiPresencas = RetrofitClient.presencasService

    // Estado interno que armazena a lista de treinos do atleta.
    private val _treinos = MutableStateFlow<List<Treino>>(emptyList())

    /**
     * Estado público que expõe a lista de treinos do atleta para observação.
     */
    val treinos: StateFlow<List<Treino>> = _treinos

    /**
     * Carrega os treinos do atleta para um dia específico da semana.
     *
     * @param idSocio O ID do sócio (atleta) cujos treinos devem ser carregados.
     * @param diaSemana O dia da semana para o qual os treinos devem ser carregados.
     */
    fun carregarTreinos(idSocio: Int, diaSemana: String) {
        viewModelScope.launch {
            try {
                // Faz a chamada ao serviço remoto para obter os treinos do atleta.
                val resposta = apiTreinos.getTreinosAluno(
                    TreinosRequest(idSocio = idSocio, diaSemana = diaSemana)
                )
                _treinos.value = resposta
            } catch (e: Exception) {
                // Em caso de erro, define a lista de treinos como vazia.
                _treinos.value = emptyList()
            }
        }
    }

    /**
     * Detecta o dia da semana atual.
     *
     * @return Uma string representando o dia da semana atual (e.g., "SEG", "TER").
     */
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

    /**
     * Carrega treinos hardcoded para testes ou desenvolvimento.
     *
     * @param treinos Lista de treinos a serem carregados.
     */
    @TestOnly
    fun carregarTreinosTest(treinos: List<Treino>) {
        _treinos.value = treinos
    }
}