package estga.dadm.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Classe principal da aplicação Spring Boot.
 *
 * Esta classe é responsável por marcar o ponto de entrada da aplicação,
 * utilizando a anotação `@SpringBootApplication` para habilitar a configuração automática,
 * a varredura de componentes e outras funcionalidades do Spring Boot.
 */
@SpringBootApplication
class BackendApplication

/**
 * Função main para inicializar a aplicação.
 *
 * Esta função é o ponto de entrada da aplicação, responsável por iniciar o contexto
 * do Spring Boot e executar a aplicação.
 *
 * @param args Argumentos de linha de comando passados para a aplicação.
 */
fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}