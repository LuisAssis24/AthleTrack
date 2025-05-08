package estga.dadm.backend.keys

import java.io.Serializable

data class SocioModalidadeId(
    val socio: Int = 0,
    val modalidade: Int = 0
) : Serializable

data class EventoModalidadeId(
    val evento: Int = 0,
    val modalidade: Int = 0
) : Serializable


