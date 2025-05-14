package estga.dadm.backend.repository

import estga.dadm.backend.model.Modalidade
import org.springframework.data.jpa.repository.JpaRepository

interface ModalidadeRepository : JpaRepository<Modalidade, Int> {

}
