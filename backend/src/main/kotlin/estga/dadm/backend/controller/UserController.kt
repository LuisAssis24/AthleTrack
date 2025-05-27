package estga.dadm.backend.controller

import estga.dadm.backend.dto.user.*
import estga.dadm.backend.model.SocioModalidade
import estga.dadm.backend.model.User
import estga.dadm.backend.repository.*
import estga.dadm.backend.services.PasswordUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Controlador REST responsável pelas operações relacionadas a utilizadores.
 *
 * Fornece endpoints para ‘login’, listagem, criação e eliminação de utilizadores,
 * bem como associação de utilizadores a modalidades.
 */
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val modalidadeRepository: ModalidadeRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository
) {
    /**
     * Realiza ‘login’ do utilizador.
     *
     * @param request Objeto LoginRequestDTO contendo o ID do sócio e a senha.
     * @return ResponseEntity com UserResponseDTO se autenticado, ou 401 se falhar.
     */
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

    /**
     * Lista todos os utilizadores, ordenando por tipo (professor, atleta, outros).
     *
     * @return Lista de UserResponseDTO representando todos os usuários.
     */
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

    /**
     * Cria um novo usuário e associa às modalidades informadas.
     *
     * @param request Objeto UserCreateRequestDTO com dados do usuário e IDs das modalidades.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
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

    /**
     * Elimina um usuário, validando a senha do solicitante.
     * Remove também todas as associações do usuário com modalidades.
     *
     * @param loginRequest Objeto LoginRequestDTO com credenciais do solicitante.
     * @param idParaEliminar ID do usuário a ser eliminado.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/eliminar/{idParaEliminar}")
    fun eliminarUser(
        @RequestBody loginRequest: LoginRequestDTO,
        @PathVariable idParaEliminar: Int
    ): ResponseEntity<String> {
        val autenticado = userRepository.findById(loginRequest.idSocio).orElse(null)

        if (autenticado == null || !PasswordUtil.matches(loginRequest.password, autenticado.password)) {
            return ResponseEntity.status(401).body("Senha inválida.")
        }

        val user = userRepository.findById(idParaEliminar).orElse(null)
        val socioModalidades = socioModalidadeRepository.findBySocioId(idParaEliminar)

        return if (user != null) {
            socioModalidades.forEach { socioModalidade ->
                socioModalidadeRepository.delete(socioModalidade)
            }
            userRepository.delete(user)
            ResponseEntity.ok("Usuário eliminado com sucesso.")
        } else {
            ResponseEntity.status(404).body("Usuário não encontrado.")
        }
    }
}