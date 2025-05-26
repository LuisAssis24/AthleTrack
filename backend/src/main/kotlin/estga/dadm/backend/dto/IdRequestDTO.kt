package estga.dadm.backend.dto

/**
 * DTO genérico para requisições que exigem apenas um ID.
 * Utilizado para transferir o identificador entre as camadas da aplicação.
 */
data class IdRequestDTO (
    val id: Int // ID utilizado na requisição
)