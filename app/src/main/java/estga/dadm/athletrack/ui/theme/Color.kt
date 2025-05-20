package estga.dadm.athletrack.ui.theme

import androidx.compose.ui.graphics.Color

val Gray = hexToColor("#B0BEC5")
val White = hexToColor("#FFFFFF")
val Black = hexToColor("#000000")

val BackgroundBlueDark = hexToColor("#0D1B2A")
val CardBlue = hexToColor("#1F4164")
val BlueAccent = hexToColor("#346CA8")

val GreenSuccess = hexToColor("#43A047")
val RedError = hexToColor("#FF5B58")

fun hexToColor(hex: String): Color {
    require(hex.startsWith("#") && (hex.length == 7 || hex.length == 9)) {
    }
    val colorValue = hex.removePrefix("#").toLong(16)
    return if (hex.length == 7) {
        Color(0xFF000000 or colorValue) // Adiciona opacidade total (FF) se não estiver presente
    } else {
        Color(colorValue)
    }
}
