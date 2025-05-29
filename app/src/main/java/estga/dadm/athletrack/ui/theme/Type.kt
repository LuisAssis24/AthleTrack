package estga.dadm.athletrack.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Define o conjunto de estilos tipográficos utilizados no aplicativo AthleTrack.
 *
 * Os estilos incluem:
 * - `displayLarge`: Estilo para textos de destaque, como títulos principais.
 * - `titleLarge`: Estilo para títulos grandes.
 * - `titleMedium`: Estilo para títulos médios.
 * - `bodyMedium`: Estilo para textos do corpo principal.
 * - `labelMedium`: Estilo para rótulos médios.
 * - `labelSmall`: Estilo para rótulos pequenos.
 */
val Typography: Typography = Typography(

    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
    ),

    /**
     * Estilo para textos de destaque, como títulos principais.
     * - Fonte padrão.
     * - Peso da fonte: Negrito.
     * - Tamanho da fonte: 24sp.
     */
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    /**
     * Estilo para títulos grandes.
     * - Fonte padrão.
     * - Peso da fonte: Negrito.
     * - Tamanho da fonte: 20sp.
     */
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    ),
    /**
     * Estilo para títulos médios.
     * - Fonte padrão.
     * - Peso da fonte: Negrito.
     * - Tamanho da fonte: 18sp.
     */
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
    ),
    /**
     * Estilo para textos do corpo principal.
     * - Fonte padrão.
     * - Tamanho da fonte: 16sp.
     */
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 16.sp,
    ),
    /**
     * Estilo para rótulos médios.
     * - Fonte padrão.
     * - Tamanho da fonte: 14sp.
     */
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 14.sp,
    ),
    /**
     * Estilo para rótulos pequenos.
     * - Fonte padrão.
     * - Tamanho da fonte: 12sp.
     */
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp,
    ),
)