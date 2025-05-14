package estga.dadm.backend.controller

import estga.dadm.backend.dto.treino.*
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.model.Treino
import estga.dadm.backend.repository.*
import org.springframework.http.ResponseEntity
import java.time.LocalTime

@RestController
@RequestMapping("/api/treinos")
class TreinoController(
    private val treinoRepository: TreinoRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository,
    private val modalidadeRepository: ModalidadeRepository,
    private val userRepository: UserRepository
) {

    @PostMapping()
    fun listarTodosOsTreinos(): List<TreinoProfResponseDTO> {
        val ordem = ordenarDias("SEG")

        return ordem.flatMap { dia ->
            treinoRepository
                .findAll()
                .filter { it.diaSemana == dia }
                .sortedBy { it.hora }
                .map { treino ->
                    TreinoProfResponseDTO(
                        nomeModalidade = treino.modalidade.nomeModalidade,
                        diaSemana = treino.diaSemana,
                        hora = treino.hora.toString(),
                        qrCode = treino.qrCode
                    )
                }
        }
    }

    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: TreinoRequestDTO): List<TreinoProfResponseDTO> {
        return treinoRepository
            .findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.idSocio, request.diaSemana)
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora.toString(),  // irá mostrar no formato HH:MM
                    qrCode = treino.qrCode
                )
            }
    }

    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: TreinoRequestDTO): List<TreinoProfResponseDTO> {
        val amanha = calculaAmanha(request.diaSemana)
        return treinoRepository
            .findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.idSocio, amanha)
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora.toString(),  // irá mostrar no formato HH:MM
                    qrCode = treino.qrCode
                )
            }
    }

    @PostMapping("/aluno")
    fun getTreinosAluno(@RequestBody request: TreinoRequestDTO): List<TreinoAlunoResponseDTO> {
        val socioModalidades = socioModalidadeRepository.findBySocioId(request.idSocio)
        val modalidades = socioModalidades.map { it.modalidade.id }
        val diasList = ordenarDias(request.diaSemana)
        val todosTreinos = mutableListOf<Treino>()

        val agora = LocalTime.now()
        val margem = agora.minusMinutes(30)

        diasList.forEach { dia ->
            val treinosFiltrados = treinoRepository
                .findByModalidadeIdInAndDiaSemanaOrderByHoraAsc(modalidades, dia)
                .filter { treino ->
                    dia != request.diaSemana || treino.hora >= margem
                }
            todosTreinos.addAll(treinosFiltrados)
        }

        return todosTreinos.map { treino ->
            TreinoAlunoResponseDTO(
                nomeModalidade = treino.modalidade.nomeModalidade,
                diaSemana = treino.diaSemana,
                hora = treino.hora.toString()
            )
        }
    }

    @PostMapping("/criar")
    fun criarTreino(@RequestBody request: TreinoCriarRequestDTO): ResponseEntity<String> {
        val professor = userRepository.findById(request.idProfessor).orElseThrow()
        val modalidade = modalidadeRepository.findById(request.idModalidade).orElseThrow()

        val qrCode = "${request.diaSemana}-${request.hora}-P${request.idProfessor}-M${request.idModalidade}"

        if (treinoRepository.findByQrCode(qrCode) != null) {
            return ResponseEntity.badRequest().body("QR Code já existe.")
        }

        val treino = Treino(
            diaSemana = request.diaSemana,
            hora = LocalTime.parse(request.hora),
            qrCode = qrCode,
            modalidade = modalidade,
            professor = professor
        )

        treinoRepository.save(treino)
        return ResponseEntity.ok("Treino criado com sucesso.")
    }

    @PostMapping("/apagar")
    fun apagarTreino(@RequestBody request: TreinoApagarRequest): ResponseEntity<String> {
        val treino = treinoRepository.findByQrCode(request.qrCode)
        if (treino != null) {
            treinoRepository.delete(treino)
            return ResponseEntity.ok("Treino apagado com sucesso.")
        }
        return ResponseEntity.badRequest().body("Treino não encontrado.")
    }

    fun calculaAmanha(dia: String): String? {
        when (dia) {
            "SEG" -> return "TER"
            "TER" -> return "QUA"
            "QUA" -> return "QUI"
            "QUI" -> return "SEX"
            "SEX" -> return "SAB"
            "SAB" -> return "DOM"
            "DOM" -> return "SEG"
        }
        return null
    }

    fun ordenarDias(dia: String): List<String?> {
        when (dia) {
            "SEG" -> return listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB", "DOM")
            "TER" -> return listOf("TER", "QUA", "QUI", "SEX", "SAB", "DOM", "SEG")
            "QUA" -> return listOf("QUA", "QUI", "SEX", "SAB", "DOM", "SEG", "TER")
            "QUI" -> return listOf("QUI", "SEX", "SAB", "DOM", "SEG", "TER", "QUA")
            "SEX" -> return listOf("SEX", "SAB", "DOM", "SEG", "TER", "QUA", "QUI")
            "SAB" -> return listOf("SAB", "DOM", "SEG", "TER", "QUA", "QUI", "SEX")
            "DOM" -> return listOf("DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB")
        }
        return emptyList()
    }
}