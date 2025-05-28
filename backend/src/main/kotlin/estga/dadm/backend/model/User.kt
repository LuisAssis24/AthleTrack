package estga.dadm.backend.model

import jakarta.persistence.*

/**
 * Entidade que representa um usuário no sistema.
 *
 * @property id Identificador único do usuário (chave primária).
 * @property nome Nome do usuário.
 * @property tipo Tipo do usuário (ex: sócio, professor, etc).
 * @property password Senha do usuário.
 * @property modalidades Lista de associações entre o usuário (como sócio) e modalidades.
 */
@Entity
@Table(name = "users")
data class User(

    /** Identificador único do usuário (chave primária). */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    /** Nome do usuário. */
    val nome: String,

    /** Tipo do usuário (ex: sócio, professor, etc). */
    val tipo: String,

    /** Senha do usuário. */
    val password: String,

    /** Lista de associações entre o usuário (como sócio) e modalidades. */
    @OneToMany(mappedBy = "socio")
    val modalidades: List<SocioModalidade> = emptyList()
)