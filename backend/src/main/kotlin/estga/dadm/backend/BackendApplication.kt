package estga.dadm.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Classe principal da aplicação Spring Boot.
 */
@SpringBootApplication
class BackendApplication

/**
 * Função main para inicializar a aplicação.
 */
fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}