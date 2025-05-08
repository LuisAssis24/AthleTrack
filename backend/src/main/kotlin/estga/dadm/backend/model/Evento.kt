package estga.dadm.backend.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "eventos")
data class Evento(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val localEvento: String,

    val data: LocalDate,

    val hora: LocalTime,

    val descricao: String,

    @OneToMany(mappedBy = "evento")
    val modalidades: List<EventoModalidade> = emptyList()
)