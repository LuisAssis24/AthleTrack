package estga.dadm.backend.controller

import estga.dadm.backend.repository.TreinoRepository
import estga.dadm.backend.dto.ProfessorIdDTO
import estga.dadm.backend.dto.TreinoDTO
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val hoje = LocalDate.now().toString()
        return treinoRepository
            .findByProfessorIdSocioAndData(request.id_professor, hoje)
            .map { treino ->
                TreinoDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    data = treino.data,
                    hora = treino.hora
                )
            }
    }

    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val amanha = LocalDate.now().plusDays(1).toString()
        return treinoRepository
            .findByProfessorIdSocioAndData(request.id_professor, amanha)
            .map { treino ->
                TreinoDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    data = treino.data,
                    hora = treino.hora
                )
            }
    }
}