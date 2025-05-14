package estga.dadm.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val nome: String,

    val tipo: String,

    val password: String,

    @OneToMany(mappedBy = "socio")
    val modalidades: List<SocioModalidade> = emptyList()
)
