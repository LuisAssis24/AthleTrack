package estga.dadm.backend.controller

import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.treino.PresencaListResponseDTO
import estga.dadm.backend.dto.treino.PresencaRequestDTO
import estga.dadm.backend.dto.treino.PresencaResponseDTO
import estga.dadm.backend.keys.PresencaId
import estga.dadm.backend.model.Presenca
import estga.dadm.backend.repository.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/presencas")
class PresencaController(
    private val userRepository: UserRepository,
    private val treinoRepository: TreinoRepository,
    private val presencaRepository: PresencaRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository
) {

    // Registra presença via QR Code
    @PostMapping("/registar")
    fun registarPresencaQr(@RequestBody request: PresencaRequestDTO): ResponseEntity<PresencaResponseDTO> {
        val treino = treinoRepository.findByQrCode(request.qrCode)
            ?: return ResponseEntity.badRequest()
                .body(PresencaResponseDTO(sucesso = false, mensagem = "QR Code inválido."))

        val aluno = userRepository.findById(request.idSocio).orElse(null)
            ?: return ResponseEntity.badRequest()
                .body(PresencaResponseDTO(sucesso = false, mensagem = "Sócio não encontrado."))

        val modalidadesDoAluno = socioModalidadeRepository.findBySocioId(request.idSocio)
            .map { it.modalidade.id }

        if (treino.modalidade.id !in modalidadesDoAluno) {
            return ResponseEntity.status(403)
                .body(PresencaResponseDTO(sucesso = false, mensagem = "Aluno não inscrito nesta modalidade."))
        }

        val presencaId = PresencaId(
            socio = request.idSocio,
            treino = treino.id
        )

        if (presencaRepository.existsById(presencaId)) {
            return ResponseEntity.status(208)
                .body(PresencaResponseDTO(sucesso = false, mensagem = "Presença já confirmada anteriormente."))
        }

        val novaPresenca = Presenca(
            socio = aluno,
            treino = treino,
            estado = true,
            qrCode = true
        )

        presencaRepository.save(novaPresenca)

        return ResponseEntity.ok(PresencaResponseDTO(sucesso = true, mensagem = "Presença registada com sucesso."))
    }

    // Registra presenças manualmente (lista de presenças)
    @PostMapping("/registarmanual")
    fun registarPresencasManuais(@RequestBody requests: List<PresencaRequestDTO>): ResponseEntity<Boolean> {
        return try {
            requests.forEach { request ->
                if (request.qrCode.isBlank()) return@forEach

                val treino = treinoRepository.findByQrCode(request.qrCode) ?: return@forEach
                val aluno = userRepository.findById(request.idSocio).orElse(null) ?: return@forEach
                val presencaId = PresencaId(aluno.id, treino.id)
                val presencaExistente = presencaRepository.findById(presencaId).orElse(null)

                if (presencaExistente != null) {
                    presencaExistente.estado = request.estado
                    presencaRepository.save(presencaExistente)
                } else {
                    val nova = Presenca(
                        socio = aluno,
                        treino = treino,
                        estado = request.estado,
                        qrCode = false
                    )
                    presencaRepository.save(nova)
                }
            }

            ResponseEntity.ok(true)
        } catch (e: Exception) {
            println("Erro ao guardar presenças manuais: ${e.message}")
            ResponseEntity.internalServerError().body(false)
        }
    }

    // Lista presenças de um treino específico
    @PostMapping("/listar")
    fun listarPresencas(@RequestBody request: IdRequestDTO): List<PresencaListResponseDTO> {
        val treino = treinoRepository.findById(request.id).orElse(null)
        if (treino == null) {
            return emptyList()
        }

        val modalidade = treino.modalidade

        val alunos = socioModalidadeRepository.findByModalidadeId(modalidade.id)
            .map { it.socio }

        val presencasSimuladas = alunos.map { socio ->
            PresencaListResponseDTO(
                id = socio.id,
                nome = socio.nome,
                estado = false,
                qrCode = false
            )
        }

        presencasSimuladas.forEach { presenca ->
            val presencaReal = presencaRepository.findBySocioIdAndTreinoId(
                socioId = presenca.id,
                treinoId = request.id
            )

            if (presencaReal?.estado == true) {
                if (presencaReal.qrCode) {
                    presenca.estado = true
                    presenca.qrCode = true
                } else if (!presencaReal.qrCode) {
                    presenca.estado = true
                    presenca.qrCode = false
                }
            } else {
                presenca.estado = false
                presenca.qrCode = false
            }
        }

        return presencasSimuladas
    }
}