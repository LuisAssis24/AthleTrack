package estga.dadm.backend.model

import estga.dadm.backend.keys.EventoModalidadeId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

class Presenca {
    @Entity
    @Table(name = "presencas")
    data class EventoModalidade(

        @Id
        @ManyToOne
        @JoinColumn(name = "id_treino")
        val treino: Treino,

        @Id
        @ManyToOne
        @JoinColumn(name = "id_aluno")
        val aluno: User,

        val estado: Boolean
    )
}