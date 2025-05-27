package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.*
import estga.dadm.athletrack.api.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel responsável pela gestão de presenças em treinos.
 */
class GestaoPresencasViewModel : ViewModel() {

    // Estado interno que armazena as informações do treino atual.
    private val _treinoInfo = MutableStateFlow<Treino?>(null)

    /**
     * Estado público que expõe as informações do treino atual para observação.
     */
    val treinoInfo: StateFlow<Treino?> = _treinoInfo

    // Estado interno que armazena a lista de alunos e suas presenças.
    private val _alunos = MutableStateFlow<List<PresencaListResponse>>(emptyList())

    /**
     * Estado público que expõe a lista de alunos e suas presenças para observação.
     */
    val alunos: StateFlow<List<PresencaListResponse>> = _alunos

    // Serviço para operações relacionadas a treinos.
    private val treinosService = RetrofitClient.treinosService

    // Serviço para operações relacionadas a presenças.
    private val presencasService = RetrofitClient.presencasService

    /**
     * Carrega as presenças de um treino com base no QR Code e no ID do sócio.
     *
     * @param qrCode O QR Code do treino.
     * @param idSocio O ID do sócio que está realizando a consulta.
     */
    fun carregarPresencas(qrCode: String, idSocio: Int) {
        viewModelScope.launch {
            try {
                // Buscar treino pelo QR Code.
                val treino = treinosService.listarTodosOsTreinos(idRequest(idSocio))
                    .find { it.qrCode == qrCode }

                if (treino != null) {
                    _treinoInfo.value = treino

                    // Buscar todos os atletas inscritos na modalidade do treino.
                    val atletasInscritos = presencasService.listarPresencas(idRequest(treino.idTreino))

                    // Atualizar estado dos atletas com presença registrada.
                    _alunos.value = atletasInscritos.map { atleta ->
                        atleta.copy(estado = atleta.estado)
                    }
                } else {
                    // Exibir mensagem caso o treino não seja encontrado.
                    android.widget.Toast.makeText(
                        android.app.Application().applicationContext,
                        "Treino não encontrado para o QR Code fornecido.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Imprimir erro no log em caso de falha.
                e.printStackTrace()
            }
        }
    }

    /**
     * Atualiza o estado de presença de um aluno.
     *
     * @param idAluno O ID do aluno cuja presença será atualizada.
     * @param presente Indica se o aluno está presente (true) ou ausente (false).
     */
    fun atualizarPresenca(idAluno: Int, presente: Boolean) {
        _alunos.value = _alunos.value.map { aluno ->
            if (aluno.id == idAluno) aluno.copy(estado = presente) else aluno
        }
    }

    /**
     * Salva as presenças manuais registradas para um treino.
     *
     * @param qrCode O QR Code do treino.
     */
    fun salvarPresencas(qrCode: String) {
        viewModelScope.launch {
            try {
                // Filtrar apenas as presenças registradas manualmente.
                val listaManual = _alunos.value
                    .filter { !it.qrCode } // Apenas presenças manuais.
                    .map {
                        PresencaRequest(
                            idSocio = it.id,
                            qrCode = qrCode,
                            estado = it.estado
                        )
                    }

                // Registrar as presenças manuais no serviço remoto.
                if (listaManual.isNotEmpty()) {
                    presencasService.registarPresencasManuais(listaManual)
                }

            } catch (e: Exception) {
                // Imprimir erro no log em caso de falha.
                e.printStackTrace()
            }
        }
    }


    /**
     * Carrega as presenças de um treino com dados hardcoded para testes.
     *
     * @param treino O treino a ser carregado.
     * @param presencas A lista de presenças a ser carregada.
     */
    fun carregarPresencasHardcoded(treino: Treino?, presencas: List<PresencaListResponse>) {
        _treinoInfo.value = treino
        _alunos.value = presencas
    }

    /**
     * Simula a criação de uma presença manual para testes.
     *
     * @param qrCode O QR Code do treino.
     * @param simularErro Indica se deve simular um erro no salvamento.
     */
    fun salvarPresencasHardcoded(qrCode: String, simularErro: Boolean = false) {
        if (simularErro) {
            // Simula erro no salvamento
            return
        }
        // Simula sucesso no salvamento
    }

}