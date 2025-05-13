package estga.dadm.backend.repository

import estga.dadm.backend.keys.PresencaId
import estga.dadm.backend.model.Presenca
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PresencaRepository : JpaRepository<Presenca, PresencaId>{

}

