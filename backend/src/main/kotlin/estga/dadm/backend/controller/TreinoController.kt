package estga.dadm.backend.controller

import estga.dadm.backend.dto.IdRequestDTO
import estga.dadm.backend.dto.treino.*
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.model.Treino
import estga.dadm.backend.repository.*
import estga.dadm.backend.services.PasswordUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalTime

/**
 * Controlador REST responsável pelas operações relacionadas a treinos.
 *
 * Fornece endpoints para listar treinos de professores e alunos, criar e apagar treinos,
 * além de utilitários para manipulação de dias da semana.
 */
@RestController
@RequestMapping("/api/treinos")
class TreinoController(
    private val treinoRepository: TreinoRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository,
    private val modalidadeRepository: ModalidadeRepository,
    private val userRepository: UserRepository,
    private val presencaRepository: PresencaRepository
) {
    /**
     * Lista todos os treinos de um professor, ordenados por dia da semana.
     *
     * @param request Objeto contendo o ‘ID’ do professor.
     * @return Lista de treinos do professor, agrupados por dia da semana.
     */
    @PostMapping
    fun listarTodosOsTreinos(@RequestBody request: IdRequestDTO): List<TreinoProfResponseDTO> {

        val ordem = ordenarDias("SEG") // ou usar dia atual

        val treinos = treinoRepository.findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.id, "SEG")

        treinos.forEach {
            println("Treino ${it.id} -> prof=${it.professor.id}, mod=${it.modalidade.id}, dia=${it.diaSemana}")
        }
        return ordem.flatMap { dia ->
            treinoRepository
                .findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.id, dia)
                .map { treino ->
                    TreinoProfResponseDTO(
                        idTreino = treino.id,
                        nomeModalidade = treino.modalidade.nomeModalidade,
                        diaSemana = treino.diaSemana,
                        hora = treino.hora.toString(),
                        qrCode = treino.qrCode
                    )
                }
        }
    }

    /**
     * Lista os treinos do professor para o dia atual, considerando uma margem de 4 horas.
     *
     * @param request Objeto contendo o ‘ID’ do professor e o dia da semana.
     * @return Lista de treinos do professor para o dia atual.
     */
    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: TreinoRequestDTO): List<TreinoProfResponseDTO> {
        val agora = LocalTime.now()
        val margem = agora.minusMinutes(240)// margem de 4 horas

        return treinoRepository
            .findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.idSocio, request.diaSemana)
            .filter {
                it.hora >= margem
            }
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    idTreino = treino.id,
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora.toString(),
                    qrCode = treino.qrCode
                )
            }
    }

    /**
     * Lista os treinos do professor para o dia seguinte.
     *
     * @param request Objeto contendo o ‘ID’ do professor e o dia da semana.
     * @return Lista de treinos do professor para o dia seguinte.
     */
    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: TreinoRequestDTO): List<TreinoProfResponseDTO> {
        val amanha = calculaAmanha(request.diaSemana)
        return treinoRepository
            .findByProfessorIdAndDiaSemanaOrderByHoraAsc(request.idSocio, amanha)
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    idTreino = treino.id,
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora.toString(),
                    qrCode = treino.qrCode
                )
            }
    }

    /**
     * Lista treinos disponíveis para um aluno, considerando as suas modalidades e horários.
     *
     * @param request Objeto contendo o ‘ID’ do sócio e o dia da semana.
     * @return Lista de treinos disponíveis para o aluno.
     */
    @PostMapping("/aluno")
    fun getTreinosAluno(@RequestBody request: TreinoRequestDTO): List<TreinoAlunoResponseDTO> {
        val socioModalidades = socioModalidadeRepository.findBySocioId(request.idSocio)
        val modalidades = socioModalidades.map { it.modalidade.id }
        val diasList = ordenarDias(request.diaSemana)
        val todosTreinos = mutableListOf<Treino>()

        val agora = LocalTime.now()
        val margem = agora.minusMinutes(60)

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

    /**
     * Cria um treino, gerando um QR code único para identificação.
     *
     * @param request Objeto contendo os dados necessários para criação do treino.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
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

    /**
     * Apaga um treino, validando o professor e a senha fornecida.
     * Remove também todas as presenças associadas ao treino.
     *
     * @param request Objeto contendo o ‘ID’ do professor, senha e QR code do treino.
     * @return ResponseEntity com mensagem de sucesso ou erro.
     */
    @PostMapping("/apagar")
    fun apagarTreino(@RequestBody request: TreinoApagarRequest): ResponseEntity<String> {
        val professor = userRepository.findById(request.idSocio).orElse(null)
            ?: return ResponseEntity.badRequest().body("Professor não encontrado.")

        if (!PasswordUtil.matches(request.password, professor.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Não foi possível apagar o treino: palavra-passe inválida.")
        }

        val treino = treinoRepository.findByQrCode(request.qrCode)
            ?: return ResponseEntity.badRequest().body("Treino não encontrado.")

        if (treino.professor.id != professor.id) {
            return ResponseEntity.status(403).body("Este treino não pertence a este professor.")
        }

        // Apaga todas as presenças associadas a este treino
        val presencas = presencaRepository.findByTreinoId(treino.id)
        presencas.forEach { presenca ->
            presencaRepository.delete(presenca)
        }

        treinoRepository.delete(treino)
        return ResponseEntity.ok("Treino apagado com sucesso.")
    }

    /**
     * Calcula o próximo dia da semana a partir do dia informado.
     *
     * @param dia Dia da semana atual (ex: "SEG").
     * @return Próximo dia da semana.
     */
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

    /**
     * Retorna a ordem dos dias da semana a partir de um dia específico.
     *
     * @param dia Dia da semana inicial.
     * @return Lista ordenada dos dias da semana começando pelo dia informado.
     */
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