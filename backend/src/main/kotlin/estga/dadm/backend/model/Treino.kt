package estga.dadm.backend.model

import jakarta.persistence.*
import java.time.LocalTime


@Entity
@Table(name = "treinos")
data class Treino(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val diaSemana: String,

    val hora: LocalTime,

    @Column(unique = true)
    val qrCode: String,

    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade,

    @ManyToOne
    @JoinColumn(name = "id_professor")
    val professor: User
)