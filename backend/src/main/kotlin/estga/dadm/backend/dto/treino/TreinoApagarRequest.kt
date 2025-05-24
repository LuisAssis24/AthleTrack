package estga.dadm.backend.dto.treino

data class TreinoApagarRequest (
    val qrCode: String,
    val idSocio: Int,
    val password: String
)