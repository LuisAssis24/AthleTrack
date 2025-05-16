package estga.dadm.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import estga.dadm.backend.keys.SocioModalidadeId
import jakarta.persistence.*

@Entity
@IdClass(SocioModalidadeId::class)
@Table(name = "socios_modalidades")
data class SocioModalidade(

    @Id
    @ManyToOne
    @JoinColumn(name = "id_socio")
    @JsonIgnore
    val socio: User,

    @Id
    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade
)

