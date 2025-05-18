package estga.dadm.backend.controller

import estga.dadm.backend.dto.user.LoginRequestDTO
import estga.dadm.backend.dto.user.UserResponseDTO
import estga.dadm.backend.dto.user.UserCreateRequestDTO
import estga.dadm.backend.dto.user.UserDeleteRequestDTO
import estga.dadm.backend.model.SocioModalidade
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.ModalidadeRepository
import estga.dadm.backend.repository.SocioModalidadeRepository
import estga.dadm.backend.repository.UserRepository
import estga.dadm.backend.security.PasswordUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository,
                     private val modalidadeRepository: ModalidadeRepository,
                     private val socioModalidadeRepository: SocioModalidadeRepository)
{
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<UserResponseDTO> {
        val user = userRepository.findById(request.idSocio).orElse(null)

        if (user != null && PasswordUtil.matches(request.password, user.password)) {
            val response = UserResponseDTO(
                idSocio = user.id,
                nome = user.nome,
                tipo = user.tipo
            )
            return ResponseEntity.ok(response)
        }

        return ResponseEntity.status(401).build()
    }

   @PostMapping("/listar")
   fun listarTodos(): List<UserResponseDTO> {
       val users = userRepository.findAll()
           .sortedBy { user ->
               when (user.tipo.lowercase()) {
                   "professor" -> 0
                   "atleta" -> 1
                   else -> 2
               }
           }
           .map { user ->
               UserResponseDTO(
                   idSocio = user.id,
                   nome = user.nome,
                   tipo = user.tipo
               )
           }

       return users
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

    @PostMapping("/eliminar")
    fun eliminarUser(@RequestBody request: UserDeleteRequestDTO): ResponseEntity<String> {
        return try {
            val user = userRepository.findById(request.idSocio).orElse(null)
            val socioModalidades = socioModalidadeRepository.findBySocioId(request.idSocio)

            if (user != null) {
                socioModalidades.forEach { socioModalidade ->
                    socioModalidadeRepository.delete(socioModalidade)
                }
                // Eliminar o usuário
                userRepository.delete(user)
                ResponseEntity.ok("Usuário eliminado com sucesso.")
            } else {
                ResponseEntity.status(404).body("Usuário não encontrado.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(500).body("Erro ao eliminar utilizador: ${e.message}")
        }
    }
}
