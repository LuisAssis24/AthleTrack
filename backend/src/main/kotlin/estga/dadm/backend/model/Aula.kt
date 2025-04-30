package estga.dadm.backend.model

import java.time.LocalDate
import java.time.LocalTime
import jakarta.persistence.*

@Entity
@Table(name = "aulas")
data class Aula(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val nomeAula: String,

    val data: LocalDate,

    val hora: LocalTime,

    val professor: String
)