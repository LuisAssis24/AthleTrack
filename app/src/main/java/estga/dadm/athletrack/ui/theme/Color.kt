package estga.dadm.athletrack.ui.theme

import androidx.compose.ui.graphics.Color

val Gray: Color = hexToColor("#B0BEC5")
val White: Color = hexToColor("#FFFFFF")
val Black: Color = hexToColor("#000000")

val BackgroundBlueDark: Color = hexToColor("#0D1B2A")
val CardBlue: Color = hexToColor("#1F4164")
val BlueAccent: Color = hexToColor("#346CA8")

val GreenSuccess: Color = hexToColor("#43A047")
val RedError: Color = hexToColor("#FF5B58")

val Transparent: Color = hexToColor("#00000000")

/**
 * Converte um código hexadecimal de cor em um objeto `Color`.
 *
 * @param hex Código hexadecimal da cor no formato `#RRGGBB` ou `#AARRGGBB`.
 * @return Objeto `Color` correspondente ao código hexadecimal fornecido.
 * @throws IllegalArgumentException Se o código hexadecimal não começar com `#` ou não tiver o comprimento esperado.
 */
fun hexToColor(hex: String): Color {
    require(hex.startsWith("#") && (hex.length == 7 || hex.length == 9)) {
        "O código hexadecimal deve começar com '#' e ter 7 ou 9 caracteres."
    }
    val colorValue = hex.removePrefix("#").toLong(16)
    return if (hex.length == 7) {
        // Adiciona opacidade total (FF) se não estiver presente no código hexadecimal.
        Color(0xFF000000 or colorValue)
    } else {
        Color(colorValue)
    }
}
