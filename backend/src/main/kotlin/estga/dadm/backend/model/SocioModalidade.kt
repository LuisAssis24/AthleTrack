package estga.dadm.backend.model

import com.fasterxml.jackson.annotation.JsonIgnore
import estga.dadm.backend.keys.SocioModalidadeId
import jakarta.persistence.*

/**
 * Entidade que representa a associação entre um sócio e uma modalidade.
 *
 * Utiliza uma chave composta formada pelo sócio e pela modalidade.
 *
 * @property socio Sócio associado à modalidade (parte da chave composta).
 * @property modalidade Modalidade associada ao sócio (parte da chave composta).
 */
@Entity
@IdClass(SocioModalidadeId::class)
@Table(name = "socios_modalidades")
data class SocioModalidade(

    /** Sócio associado à modalidade (chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_socio")
    @JsonIgnore
    val socio: User,

    /** Modalidade associada ao sócio (chave composta). */
    @Id
    @ManyToOne
    @JoinColumn(name = "id_modalidade")
    val modalidade: Modalidade
)