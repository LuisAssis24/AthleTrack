package estga.dadm.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "treinos")
data class Treino(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val diaSemana: String,

    val hora: String,


    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade,

    @ManyToOne
    @JoinColumn(name = "id_professor")
    val professor: User
)