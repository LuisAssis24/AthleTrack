package estga.dadm.backend.controller

import estga.dadm.backend.dto.user.LoginRequestDTO
import estga.dadm.backend.dto.user.LoginResponseDTO
import estga.dadm.backend.dto.user.UserCreateRequestDTO
import estga.dadm.backend.model.SocioModalidade
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import estga.dadm.backend.repository.UserRepository
import estga.dadm.backend.security.PasswordUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository,
                     private val modalidadeRepository: ModalidadeRepository,
                     private val socioModalidadeRepository: SocioModalidadeRepository)
{
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        val user = userRepository.findById(request.idSocio).orElse(null)

        if (user != null && PasswordUtil.matches(request.password, user.password)) {
            val response = LoginResponseDTO(
                idSocio = user.id,
                nome = user.nome,
                tipo = user.tipo
            )
            return ResponseEntity.ok(response)
        }

        return ResponseEntity.status(401).build()
    }

    @PostMapping("/criar")
    fun criarUser(@RequestBody request: UserCreateRequestDTO): ResponseEntity<String> {
        return try {
            val encryptedPassword = PasswordUtil.encode(request.password)

            val user = User(
                nome = request.nome,
                password = encryptedPassword,
                tipo = request.tipo,
            )


            val savedUser = userRepository.save(user)

            request.modalidades.forEach { modalidadeId ->
                val modalidade = modalidadeRepository.findById(modalidadeId)
                    .orElseThrow { IllegalArgumentException("Modalidade $modalidadeId não encontrada.") }

                val ligacao = SocioModalidade(
                    socio = savedUser,
                    modalidade = modalidade
                )
                socioModalidadeRepository.save(ligacao)
            }

            ResponseEntity.ok("Usuário criado com sucesso.")
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body("Erro ao criar utilizador: ${e.message}")
        }
    }

}
