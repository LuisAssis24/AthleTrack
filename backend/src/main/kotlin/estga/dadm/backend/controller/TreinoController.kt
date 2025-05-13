package estga.dadm.backend.controller

import estga.dadm.backend.dto.TreinoAlunoResponseDTO
import estga.dadm.backend.repository.TreinoRepository
import estga.dadm.backend.dto.TreinoRequestDTO
import estga.dadm.backend.dto.TreinoProfResponseDTO
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.model.Treino
import estga.dadm.backend.repository.SocioModalidadeRepository

@RestController
@RequestMapping("/api/treinos")
class TreinoController(
    private val treinoRepository: TreinoRepository,
    private val socioModalidadeRepository: SocioModalidadeRepository
) {

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

        diasList.forEach { dia ->
            val treinos = treinoRepository.findByModalidadeIdInAndDiaSemanaOrderByHoraAsc(modalidades, request.diaSemana)

            if (treinos.isNotEmpty()) {
                todosTreinos.addAll(treinos)
            }
        }


        return todosTreinos.map { treino: Treino ->
            TreinoAlunoResponseDTO(
                nomeModalidade = treino.modalidade.nomeModalidade,
                diaSemana = treino.diaSemana,
                hora = treino.hora.toString(),  // irá mostrar no formato HH:MM
            )
        }
    }

    fun calculaAmanha(dia: String): String? {
        when (dia) {
            "SEG" -> return "TER"
            "TER" -> return "QUA"
            "QUA" -> return "QUI"
            "QUI" -> return "SEX"
            "SEX" -> return "SAB"
            "SAB" -> return null
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