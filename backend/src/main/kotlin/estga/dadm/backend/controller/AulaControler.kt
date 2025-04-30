package estga.dadm.backend.controller

import estga.dadm.backend.model.Aula
import estga.dadm.backend.repository.AulaRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/aulas")
class AulaController(private val aulaRepository: AulaRepository) {

    @GetMapping("/hoje")
    fun listarAulasDeHoje(@RequestParam professor: String): List<Aula> {
        val hoje = LocalDate.now()
        return aulaRepository.findByNomeProfessorAndData(professor, hoje)
    }

    @GetMapping("/amanha")
    fun listarAulasDeAmanha(@RequestParam professor: String): List<Aula> {
        val amanha = LocalDate.now().plusDays(1)
        return aulaRepository.findByNomeProfessorAndData(professor, amanha)
    }
}