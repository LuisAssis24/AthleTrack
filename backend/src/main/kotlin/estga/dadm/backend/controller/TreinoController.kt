package estga.dadm.backend.controller

import estga.dadm.backend.repository.TreinoRepository
import estga.dadm.backend.dto.TreinoProfRequestDTO
import estga.dadm.backend.dto.TreinoProfResponseDTO
import org.springframework.web.bind.annotation.*
import estga.dadm.backend.model.Treino

@RestController
@RequestMapping("/api/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: TreinoProfRequestDTO): List<TreinoProfResponseDTO> {
        return treinoRepository
            .findByProfessorIdSocioAndDiaSemana(request.idProfessor, request.diaSemana)
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora
                )
            }
    }

    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: TreinoProfRequestDTO): List<TreinoProfResponseDTO> {
        val amanha = calculaAmanha(request.diaSemana)
        return treinoRepository
            .findByProfessorIdSocioAndDiaSemana(request.idProfessor, amanha)
            .map { treino: Treino ->
                TreinoProfResponseDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana,
                    hora = treino.hora
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
        }
        return null
    }
}