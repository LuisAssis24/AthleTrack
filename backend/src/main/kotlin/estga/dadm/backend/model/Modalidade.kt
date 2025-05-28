package estga.dadm.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

/**
 * Entidade que representa uma modalidade no sistema.
 *
 * @property id Identificador único da modalidade.
 * @property nomeModalidade Nome da modalidade.
 * @property socios Lista de associações entre sócios e esta modalidade.
 */
@Entity
@Table(name = "modalidades")
data class Modalidade(

    /** Identificador único da modalidade (chave primária). */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    /** Nome da modalidade. */
    val nomeModalidade: String,

    /** Lista de associações entre sócios e esta modalidade. */
    @JsonIgnore
    @OneToMany(mappedBy = "modalidade")
    val socios: List<SocioModalidade> = emptyList()
)