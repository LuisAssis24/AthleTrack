package estga.dadm.backend.model

import estga.dadm.backend.keys.EventoModalidadeId
import jakarta.persistence.*

/**
 * Entidade que representa a associação entre um evento e uma modalidade.
 *
 * Utiliza uma chave composta formada pelo evento e pela modalidade.
 *
 * @property evento Evento associado.
 * @property modalidade Modalidade associada ao evento.
 */
@Entity
@IdClass(EventoModalidadeId::class)
@Table(name = "eventos_modalidades")
data class EventoModalidade(

    /** Evento associado à modalidade (parte da chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_evento")
    val evento: Evento,

    /** Modalidade associada ao evento (parte da chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade
)