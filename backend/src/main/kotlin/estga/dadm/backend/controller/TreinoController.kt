package estga.dadm.backend.controller

import estga.dadm.backend.model.Treino
import estga.dadm.backend.repository.TreinoRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/treinos")
class TreinoController(private val treinoRepository: TreinoRepository) {

    @PostMapping("/hoje")
    fun getTreinosHoje(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val hoje = LocalDate.now()
        val inicio = hoje.atStartOfDay()
        val fim = hoje.plusDays(1).atStartOfDay()
        return treinoRepository
            .findByProfessorIdSocioAndDataHoraBetween(request.id_professor, inicio, fim)
            .map { TreinoDTO(it.modalidade.nomeModalidade, it.dataHora.toLocalTime().toString()) }
    }

    @PostMapping("/amanha")
    fun getTreinosAmanha(@RequestBody request: ProfessorIdDTO): List<TreinoDTO> {
        val amanha = LocalDate.now().plusDays(1)
        val inicio = amanha.atStartOfDay()
        val fim = amanha.plusDays(1).atStartOfDay()
        return treinoRepository
            .findByProfessorIdSocioAndDataHoraBetween(request.id_professor, inicio, fim)
            .map { treino ->
                TreinoDTO(
                    nomeModalidade = treino.modalidade.nomeModalidade,
                    data = treino.dataHora.toLocalDate().toString(),
                    hora = treino.dataHora.toLocalTime().toString()
                )
            }
    }
}

data class TreinoDTO(
    val nomeModalidade: String,
    val hora: String
)