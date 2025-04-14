package estga.dadm.backend.repository

import estga.dadm.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByIdSocioAndPassword(idSocio: Int, password: String): User?
}
