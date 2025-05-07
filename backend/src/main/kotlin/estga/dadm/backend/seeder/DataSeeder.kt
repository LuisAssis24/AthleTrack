package estga.dadm.backend.seeder

import estga.dadm.backend.model.Treino
import estga.dadm.backend.model.User
import estga.dadm.backend.model.Modalidade
import estga.dadm.backend.repository.TreinoRepository
import estga.dadm.backend.repository.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val treinoRepository: TreinoRepository,
    private val entityManager: EntityManager
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        // Evita duplicação
        if (treinoRepository.count() > 0) return

        // Garantir que o professor com id 99 existe
        val professor = userRepository.findById(99).orElseGet {
            userRepository.save(
                User(idSocio = 99, password = "1234", tipo = "professor", nome = "Carlos Silva")
            )
        }

        // Buscar modalidades diretamente com o EntityManager
        val modalidade2 = entityManager.find(Modalidade::class.java, 2)
        val modalidade3 = entityManager.find(Modalidade::class.java, 3)

        if (modalidade2 == null || modalidade3 == null) {
            println("Modalidade com ID 2 ou 3 não encontrada. Verifica a base de dados.")
            return
        }

        val diasSemana = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SAB")
        val horas = listOf(
            LocalTime.of(9, 0),
            LocalTime.of(11, 0),
            LocalTime.of(14, 0)
        )

        // Criar treinos com alternância entre modalidade 2 e 3
        var modalidadeAtual = modalidade2

        diasSemana.forEach { dia ->
            horas.forEach { hora ->
                val treino = Treino(
                    diaSemana = dia,
                    hora = hora,
                    professor = professor,
                    modalidade = modalidadeAtual,
                    qrCode = "QR-$dia-${hora}"
                )
                treinoRepository.save(treino)

                // Alterna entre as modalidades
                modalidadeAtual = if (modalidadeAtual == modalidade2) modalidade3 else modalidade2
            }
        }

        println("Treinos com modalidades 2 e 3 criados com sucesso.")
    }
}
