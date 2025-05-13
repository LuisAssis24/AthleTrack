package estga.dadm.backend.keys

import estga.dadm.backend.model.Treino
import java.io.Serializable

data class SocioModalidadeId(
    val socio: Int = 0,
    val modalidade: Int = 0
) : Serializable

data class EventoModalidadeId(
    val evento: Int = 0,
    val modalidade: Int = 0
) : Serializable

data class PresencaId(
    val socio: Int = 0,
    val treino: Int = 0
) : Serializable



