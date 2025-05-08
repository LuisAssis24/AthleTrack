package estga.dadm.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "modalidades")
data class Modalidade(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val nomeModalidade: String,

    @OneToMany(mappedBy = "modalidade")
    val socios: List<SocioModalidade> = emptyList()
)