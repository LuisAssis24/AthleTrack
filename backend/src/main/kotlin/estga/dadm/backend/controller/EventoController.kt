package estga.dadm.backend.controller

import estga.dadm.backend.model.Evento
import estga.dadm.backend.repository.EventoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/eventos")
class EventoController(private val eventoRepository: EventoRepository) {

    @GetMapping
    fun listarTodos(): List<Evento> = eventoRepository.findAll()

    @PostMapping("/data")
    fun listarPorDataPost(@RequestBody data: String): ResponseEntity<List<Evento>> {
        val eventos = eventoRepository.findByData(LocalDate.parse(data))
//        val eventos = listOf(
//            Evento(idEvento = 1, localEvento = "Ginásio Central", data = LocalDate.parse(data), hora = LocalTime.of(10, 0)),
//            Evento(idEvento = 2, localEvento = "Campo de Futebol", data = LocalDate.parse(data), hora = LocalTime.of(15, 30))
//        )
        return ResponseEntity.ok(eventos)
    }

    @PostMapping
    fun criarEvento(@RequestBody evento: Evento): ResponseEntity<Evento> {
        val novoEvento = eventoRepository.save(evento)
        return ResponseEntity.ok(novoEvento)
    }

    @DeleteMapping("/{id}")
    fun deletarEvento(@PathVariable id: Long): ResponseEntity<Void> {
        return if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id)
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}