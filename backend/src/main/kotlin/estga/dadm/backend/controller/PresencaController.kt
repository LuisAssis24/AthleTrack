package estga.dadm.backend.controller

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

    @PostMapping("/registar")
    fun registarPresenca(@RequestBody request: PresencaRequestDTO): ResponseEntity<PresencaResponseDTO> {
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
            estado = true
        )

        presencaRepository.save(novaPresenca)

        return ResponseEntity.ok(PresencaResponseDTO(sucesso = true, mensagem = "Presença registada com sucesso."))
    }
}


