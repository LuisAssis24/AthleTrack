package estga.dadm.backend.repository

import estga.dadm.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByIdAndPassword(idSocio: Int, password: String): User?
}
