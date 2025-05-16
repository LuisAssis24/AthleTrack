package estga.dadm.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "modalidades")
data class Modalidade(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val nomeModalidade: String,

    @JsonIgnore
    @OneToMany(mappedBy = "modalidade")
    val socios: List<SocioModalidade> = emptyList()
)