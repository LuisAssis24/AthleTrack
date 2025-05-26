package estga.dadm.backend.keys

import java.io.Serializable

/**
 * Classe que representa a chave composta para a relação Sócio-Modalidade.
 * Utilizada como chave primária composta em entidades JPA.
 */
data class SocioModalidadeId(
    val socio: Int = 0,         // ID do sócio
    val modalidade: Int = 0     // ID da modalidade
) : Serializable

/**
 * Classe que representa a chave composta para a relação Evento-Modalidade.
 * Utilizada como chave primária composta em entidades JPA.
 */
data class EventoModalidadeId(
    val evento: Int = 0,        // ID do evento
    val modalidade: Int = 0     // ID da modalidade
) : Serializable

/**
 * Classe que representa a chave composta para a relação Presença.
 * Utilizada como chave primária composta em entidades JPA.
 */
data class PresencaId(
    val socio: Int = 0,         // ID do sócio
    val treino: Int = 0         // ID do treino
) : Serializable