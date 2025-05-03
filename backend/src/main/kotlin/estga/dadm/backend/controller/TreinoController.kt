package estga.dadm.backend.controller

import estga.dadm.backend.repository.TreinoRepository
import estga.dadm.backend.dto.ProfessorIdDTO
import estga.dadm.backend.dto.TreinoDTO
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import estga.dadm.backend.enum.DiaSemana
import estga.dadm.backend.model.Treino

@RestController
@RequestMapping("/api/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val hoje = DiaSemana.valueOf(LocalDate.now().dayOfWeek.name)
        return treinoRepository
            .findByProfessorIdSocioAndDiaSemana(request.id_professor, hoje)
            .map { treino: Treino ->
                TreinoDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana.name,
                    hora = treino.hora
                )
            }
    }

    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val amanha = DiaSemana.valueOf(LocalDate.now().plusDays(1).dayOfWeek.name)
        return treinoRepository
            .findByProfessorIdSocioAndDiaSemana(request.id_professor, amanha)
            .map { treino: Treino ->
                TreinoDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    diaSemana = treino.diaSemana.name,
                    hora = treino.hora
                )
            }
    }
}