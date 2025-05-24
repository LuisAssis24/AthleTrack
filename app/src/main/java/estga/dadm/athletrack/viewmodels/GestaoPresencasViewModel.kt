package estga.dadm.athletrack.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import estga.dadm.athletrack.api.PresencaListResponseDTO
import estga.dadm.athletrack.api.PresencaRequest
import estga.dadm.athletrack.api.RetrofitClient
import estga.dadm.athletrack.api.Treino
import estga.dadm.athletrack.api.idRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GestaoPresencasViewModel : ViewModel() {
    private val _treinoInfo = MutableStateFlow<Treino?>(null)
    val treinoInfo: StateFlow<Treino?> = _treinoInfo

    private val _alunos = MutableStateFlow<List<PresencaListResponseDTO>>(emptyList())
    val alunos: StateFlow<List<PresencaListResponseDTO>> = _alunos

    private val treinosService = RetrofitClient.treinosService
    private val presencasService = RetrofitClient.presencasService

    fun carregarPresencas(qrCode: String, idSocio: Int) {
        viewModelScope.launch {
            try {
                // Buscar treino pelo QR Code
                val treino = treinosService.listarTodosOsTreinos(idRequest(idSocio))
                    .find { it.qrCode == qrCode }

                if (treino != null) {
                    _treinoInfo.value = treino

                    // Buscar todos os atletas inscritos na modalidade do treino
                    val atletasInscritos = presencasService.listarPresencas(idRequest(treino.idTreino))

                    // Atualizar estado dos atletas com presença registrada
                    _alunos.value = atletasInscritos.map { atleta ->
                        atleta.copy(estado = atleta.estado)
                    }
                } else {
                    android.widget.Toast.makeText(
                        android.app.Application().applicationContext,
                        "Treino não encontrado para o QR Code fornecido.",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun atualizarPresenca(idAluno: Int, presente: Boolean) {
        _alunos.value = _alunos.value.map { aluno ->
            if (aluno.id == idAluno) aluno.copy(estado = presente) else aluno
        }
    }

    fun salvarPresencas(qrCode: String) {
        viewModelScope.launch {
            _alunos.value.filter { it.estado }.forEach { aluno ->
                try {
                    presencasService.registarPresenca(
                        PresencaRequest(idSocio = aluno.id, qrCode = qrCode)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}