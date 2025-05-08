package estga.dadm.backend.controller

import estga.dadm.backend.dto.LoginRequestDTO
import estga.dadm.backend.dto.LoginResponseDTO
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class LoginController(private val userRepository: UserRepository) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val user = userRepository.findByIdAndPassword(request.idSocio, request.password)
        val response = if (user != null) {
            LoginResponseDTO(
                idSocio = user.id,
                nome = user.nome,
                tipo = user.tipo
            )
        } else {
            null
        }
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
