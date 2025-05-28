package estga.dadm.backend.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

/**
 * Entidade que representa um evento no sistema.
 *
 * @property id Identificador único do evento.
 * @property localEvento Local onde o evento será realizado.
 * @property data Data do evento.
 * @property hora Hora do evento.
 * @property descricao Descrição detalhada do evento.
 * @property modalidades Lista de modalidades associadas ao evento.
 */
@Entity
@Table(name = "eventos")
data class Evento(

    /** Identificador único do evento (chave primária). */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    /** Local onde o evento será realizado. */
    val localEvento: String,

    /** Data do evento. */
    val data: LocalDate,

    /** Hora do evento. */
    val hora: LocalTime,

    /** Descrição detalhada do evento. */
    val descricao: String,

    /** Lista de modalidades associadas ao evento. */
    @OneToMany(mappedBy = "evento")
    val modalidades: List<EventoModalidade> = emptyList()
)