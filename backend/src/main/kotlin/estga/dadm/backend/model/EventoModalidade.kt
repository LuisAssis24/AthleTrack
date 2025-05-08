package estga.dadm.backend.model

import estga.dadm.backend.keys.EventoModalidadeId
import jakarta.persistence.*

@Entity
@IdClass(EventoModalidadeId::class)
@Table(name = "eventos_modalidades")
data class EventoModalidade(

    @Id
    @ManyToOne
    @JoinColumn(name = "id_evento")
    val evento: Evento,

    @Id
    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade
)

