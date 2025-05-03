package estga.dadm.backend.controller

import estga.dadm.backend.dto.LoginRequestDTO
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class LoginController(private val userRepository: UserRepository) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<Any> {
        val user: User? = userRepository.findByIdSocioAndPassword(
            request.idSocio,
            request.password
        )

        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(401).body("Credenciais inválidas")
        }
    }
}
