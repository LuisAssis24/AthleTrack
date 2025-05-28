package estga.dadm.backend.model

import jakarta.persistence.*
import java.time.LocalTime

/**
 * Entidade que representa um treino no sistema.
 *
 * @property id Identificador único do treino (chave primária).
 * @property diaSemana Dia da semana em que o treino ocorre.
 * @property hora Hora do treino.
 * @property qrCode Código QR único associado ao treino.
 * @property modalidade Modalidade à qual o treino pertence.
 * @property professor Professor responsável pelo treino.
 */
@Entity
@Table(name = "treinos")
data class Treino(

    /** Identificador único do treino (chave primária). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    /** Dia da semana em que o treino ocorre. */
    val diaSemana: String,

    /** Hora do treino. */
    val hora: LocalTime,

    /** Código QR único associado ao treino. */
    @Column(unique = true)
    val qrCode: String,

    /** Modalidade à qual o treino pertence. */
    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade,

    /** Professor responsável pelo treino. */
    @ManyToOne
    @JoinColumn(name = "id_professor")
    val professor: User
)