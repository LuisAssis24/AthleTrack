package estga.dadm.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "user")
data class User(
    @Id
    val idSocio: Int,

    val nome: String,

    val tipo: String,

    val password: String
)
