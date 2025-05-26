package estga.dadm.backend.services

import estga.dadm.backend.repository.PresencaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

/**
 * Serviço responsável por limpar as presenças semanalmente.
 */
@Service
class PresencaCleanup(
    private val presencaRepository: PresencaRepository
) {

    /**
     * Remove todas as presenças no domingo às 23:59.
     */
    @Scheduled(cron = "0 59 23 ? * SUN")
    fun deletarPresencasNoDomingo() {
        presencaRepository.deleteAll()
        println("Presenças removidas no domingo à noite.")
    }
}