package estga.dadm.backend.model

import estga.dadm.backend.keys.PresencaId
import jakarta.persistence.*

@Entity
@IdClass(PresencaId::class)
@Table(name = "presencas")
data class Presenca(
    @Id
    @ManyToOne
    @JoinColumn(name = "id_socio")
    val socio: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "id_treino")
    val treino: Treino?,

    var estado: Boolean,

    val qrCode: Boolean
)
