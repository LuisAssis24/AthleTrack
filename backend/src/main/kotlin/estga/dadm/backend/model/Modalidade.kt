package estga.dadm.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "modalidades")
data class Modalidade(
    @Id
    @Column(name = "id_modalidade")
    val id: Int,

    @Column(name = "nome_modalidade")
    val nomeModalidade: String
)