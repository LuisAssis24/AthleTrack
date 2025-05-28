package estga.dadm.backend.model

import estga.dadm.backend.keys.PresencaId
import jakarta.persistence.*

/**
 * Entidade que representa a presença de um sócio em um treino.
 *
 * Utiliza uma chave composta formada pelo sócio e pelo treino.
 *
 * @property socio Sócio associado à presença (parte da chave composta).
 * @property treino Treino associado à presença (parte da chave composta).
 * @property estado Indica se a presença foi confirmada (true) ou não (false).
 * @property qrCode Indica se a presença foi registrada via QR Code.
 */
@Entity
@IdClass(PresencaId::class)
@Table(name = "presencas")
data class Presenca(
    /** Sócio associado à presença (chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_socio")
    val socio: User,

    /** Treino associado à presença (chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_treino")
    val treino: Treino?,

    /** Indica se a presença foi confirmada. */
    var estado: Boolean,

    /** Indica se a presença foi registrada via QR Code. */
    val qrCode: Boolean
)