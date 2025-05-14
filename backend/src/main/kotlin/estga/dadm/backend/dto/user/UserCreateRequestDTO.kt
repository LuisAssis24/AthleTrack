package estga.dadm.backend.dto.user

data class UserCreateRequestDTO(
    val password: String,
    val nome: String,
    val tipo: String,
    val modalidades: List<Int>
)