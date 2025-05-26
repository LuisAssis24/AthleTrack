package estga.dadm.backend.repository

import estga.dadm.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repositório para operações CRUD relacionadas à entidade User.
 */
@Repository
interface UserRepository : JpaRepository<User, Int> {
}